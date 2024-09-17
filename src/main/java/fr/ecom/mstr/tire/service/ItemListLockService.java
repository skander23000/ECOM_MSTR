package fr.ecom.mstr.tire.service;

import fr.ecom.mstr.tire.domain.ItemListLock;
import fr.ecom.mstr.tire.domain.Tire;
import fr.ecom.mstr.tire.repository.ItemListLockRepository;
import fr.ecom.mstr.tire.repository.TireRepository;
import fr.ecom.mstr.tire.service.dto.ItemListLockDTO;
import fr.ecom.mstr.tire.service.mapper.ItemListLockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link fr.ecom.mstr.tire.domain.ItemListLock}.
 */
@Service
@Transactional
public class ItemListLockService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemListLockService.class);

    private final TireRepository tireRepository;
    private final ItemListLockRepository itemListLockRepository;

    private final ItemListLockMapper itemListLockMapper;

    public ItemListLockService(
        ItemListLockRepository itemListLockRepository,
        ItemListLockMapper itemListLockMapper,
        TireRepository tireRepository
    ) {
        this.itemListLockRepository = itemListLockRepository;
        this.itemListLockMapper = itemListLockMapper;
        this.tireRepository = tireRepository;
    }

    /**
     * Save a itemListLock.
     *
     * @param itemListLockDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemListLockDTO save(ItemListLockDTO itemListLockDTO) {
        LOG.debug("Request to save ItemListLock : {}", itemListLockDTO);
        ItemListLock itemListLock = itemListLockMapper.toEntity(itemListLockDTO);
        itemListLock = itemListLockRepository.save(itemListLock);
        return itemListLockMapper.toDto(itemListLock);
    }

    /**
     * Update a itemListLock.
     *
     * @param itemListLockDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemListLockDTO update(ItemListLockDTO itemListLockDTO) {
        LOG.debug("Request to update ItemListLock : {}", itemListLockDTO);
        ItemListLock itemListLock = itemListLockMapper.toEntity(itemListLockDTO);
        itemListLock = itemListLockRepository.save(itemListLock);
        return itemListLockMapper.toDto(itemListLock);
    }

    /**
     * Partially update a itemListLock.
     *
     * @param itemListLockDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ItemListLockDTO> partialUpdate(ItemListLockDTO itemListLockDTO) {
        LOG.debug("Request to partially update ItemListLock : {}", itemListLockDTO);

        return itemListLockRepository
            .findById(itemListLockDTO.getId())
            .map(existingItemListLock -> {
                itemListLockMapper.partialUpdate(existingItemListLock, itemListLockDTO);

                return existingItemListLock;
            })
            .map(itemListLockRepository::save)
            .map(itemListLockMapper::toDto);
    }

    /**
     * Get all the itemListLocks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemListLockDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ItemListLocks");
        return itemListLockRepository.findAll(pageable).map(itemListLockMapper::toDto);
    }

    /**
     * Checked if there is enough stock and update lock if true.
     *
     * @param userUuid Unique universal id of the user - generated by the front end
     * @param tireId   technical id of the tire
     * @param quantity new item quantity
     * @return true if there is enough in stock .
     */
    @Transactional
    public Boolean isItemAvailable(UUID userUuid, Long tireId, Integer quantity, Boolean hasLock) {
        LOG.debug("Check if the item is available");
        if (quantity > 100 || (hasLock && Boolean.TRUE.equals(this.itemListLockRepository.hasHitShoppingCartLimit(userUuid, quantity)))) {
            return false;
        }

        Tire tire = this.tireRepository.findByIdWithOptimisticLock(tireId);
        Optional<ItemListLock> itemLockOptional = this.itemListLockRepository.findByUuidAndTireId(userUuid, tireId);
        boolean res;
        if (itemLockOptional.isPresent()) {
            // Lock does exist, update the lock
            ItemListLock itemLock = itemLockOptional.orElseThrow();
            Integer quantityDiff = quantity - itemLock.getQuantity();
            res = tire.getQuantity() - quantityDiff >= 0;
            if (res) {
                tire.setQuantity(tire.getQuantity() - quantityDiff);
                this.tireRepository.saveAndFlush(tire);
                if (quantity == 0) {
                    // The quantity has been set to 0, therefore the lock is no longer needed
                    this.itemListLockRepository.deleteById(itemLock.getId());
                } else {
                    this.itemListLockRepository.updateItemQuantity(itemLock.getId(), quantity);
                }
            }
        } else {
            // lock doesn't exist - create a new lock only if there is enough items
            ItemListLock newLock = new ItemListLock();
            res = tire.getQuantity() - quantity >= 0;
            if (res) {
                tire.setQuantity(tire.getQuantity() - quantity);
                this.tireRepository.saveAndFlush(tire);
                newLock.setLockTime(Instant.now().plus(30, ChronoUnit.MINUTES));
                newLock.setQuantity(quantity);
                newLock.setUserUuid(userUuid);
                newLock.setTire(tire);
                this.itemListLockRepository.save(newLock);
            }
        }
        if (res) {
            // updating all locked for the user
            Instant newTimer = itemLockOptional.isPresent()
                ? itemLockOptional.orElseThrow().getLockTime().plus(1, ChronoUnit.MINUTES)
                : Instant.now().plus(30, ChronoUnit.MINUTES);
            this.itemListLockRepository.updateItemTimer(newTimer, userUuid);
        }
        return res;
    }

    @Transactional(readOnly = true)
    public Boolean isUserUuidAssociatedToAnExistingLock(UUID userUuid) {
        return this.itemListLockRepository.isItemListLockByUuid(userUuid);
    }

    @Transactional
    public List<ItemListLockDTO> findAllItemListLockByUserUuid(UUID userUuid) {
        return this.itemListLockRepository.findByUuid(userUuid)
            .stream().map(this.itemListLockMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Execute a CRON every 1 minutes to get the list of expired lock.
     * IF the lock has expired, the item stock will be updated with the lock quantity and the lock deleted
     */
    @Transactional
    @Scheduled(cron = "0 */1 * * * ?")
    public void cleanupLockAndRestock() {
        List<ItemListLock> expiredLocks = this.itemListLockRepository.findAllExpiredLock(Instant.now());
        if (expiredLocks != null && !expiredLocks.isEmpty()) {
            LOG.debug("Expiring " + expiredLocks.size() + " lock(s)");
            HashMap<Long, Integer> tireMap = new HashMap<>();
            expiredLocks.forEach(lock -> tireMap.merge(lock.getTire().getId(), lock.getQuantity(), Integer::sum));
            for (Map.Entry<Long, Integer> entry : tireMap.entrySet()) {
                Tire tire = this.tireRepository.findByIdWithOptimisticLock(entry.getKey());
                tire.setQuantity(tire.getQuantity() + entry.getValue());
                this.tireRepository.saveAndFlush(tire);
            }
            this.itemListLockRepository.deleteAllById(expiredLocks.stream().map(ItemListLock::getId).collect(Collectors.toList()));
        }
    }

    /**
     * Get one itemListLock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemListLockDTO> findOne(Long id) {
        LOG.debug("Request to get ItemListLock : {}", id);
        return itemListLockRepository.findById(id).map(itemListLockMapper::toDto);
    }

    /**
     * Delete the itemListLock by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ItemListLock : {}", id);
        itemListLockRepository.deleteById(id);
    }

    /**
     * Delete the itemListLock by ids.
     *
     * @param ids the list of ids of entities.
     */
    @Transactional
    public void deleteAll(List<Long> ids) {
        LOG.debug("Request to delete ItemListLock : {}", ids);
        itemListLockRepository.deleteAllById(ids);
    }

    /**
     * Delete the itemListLock by userUuid
     *
     * @param userUuid the userUuid.
     */
    @Transactional
    public void deleteAllByUserUuid(UUID userUuid) {
        List<ItemListLock> itemListLockToDelete = this.itemListLockRepository.findByUuid(userUuid);
        if (itemListLockToDelete != null && !itemListLockToDelete.isEmpty()) {
            LOG.debug("Deleting " + itemListLockToDelete.size() + " lock(s)");
            HashMap<Long, Integer> tireMap = new HashMap<>();
            itemListLockToDelete.forEach(lock -> tireMap.merge(lock.getTire().getId(), lock.getQuantity(), Integer::sum));
            for (Map.Entry<Long, Integer> entry : tireMap.entrySet()) {
                Tire tire = this.tireRepository.findByIdWithOptimisticLock(entry.getKey());
                tire.setQuantity(tire.getQuantity() + entry.getValue());
                this.tireRepository.saveAndFlush(tire);
            }
            this.itemListLockRepository.deleteAllById(itemListLockToDelete.stream().map(ItemListLock::getId).collect(Collectors.toList()));
        }

    }
}

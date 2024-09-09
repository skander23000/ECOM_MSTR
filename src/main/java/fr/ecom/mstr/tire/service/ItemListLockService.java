package fr.ecom.mstr.tire.service;

import fr.ecom.mstr.tire.domain.ItemListLock;
import fr.ecom.mstr.tire.repository.ItemListLockRepository;
import fr.ecom.mstr.tire.service.dto.ItemListLockDTO;
import fr.ecom.mstr.tire.service.mapper.ItemListLockMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.ecom.mstr.tire.domain.ItemListLock}.
 */
@Service
@Transactional
public class ItemListLockService {

    private static final Logger LOG = LoggerFactory.getLogger(ItemListLockService.class);

    private final ItemListLockRepository itemListLockRepository;

    private final ItemListLockMapper itemListLockMapper;

    public ItemListLockService(ItemListLockRepository itemListLockRepository, ItemListLockMapper itemListLockMapper) {
        this.itemListLockRepository = itemListLockRepository;
        this.itemListLockMapper = itemListLockMapper;
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
}

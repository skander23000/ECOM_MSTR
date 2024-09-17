package fr.ecom.mstr.tire.web.rest;

import fr.ecom.mstr.tire.repository.ItemListLockRepository;
import fr.ecom.mstr.tire.service.ItemListLockService;
import fr.ecom.mstr.tire.service.dto.ItemListLockDTO;
import fr.ecom.mstr.tire.web.rest.errors.BadRequestAlertException;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * REST controller for managing {@link fr.ecom.mstr.tire.domain.ItemListLock}.
 */
@RestController
@RequestMapping("/api/item-list-locks")
public class ItemListLockResource {

    private static final Logger LOG = LoggerFactory.getLogger(ItemListLockResource.class);

    private static final String ENTITY_NAME = "itemListLock";
    private static final Pattern UUID_REGEX = Pattern.compile(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );
    private final ItemListLockService itemListLockService;
    private final ItemListLockRepository itemListLockRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public ItemListLockResource(ItemListLockService itemListLockService, ItemListLockRepository itemListLockRepository) {
        this.itemListLockService = itemListLockService;
        this.itemListLockRepository = itemListLockRepository;
    }

    /**
     * {@code POST  /item-list-locks} : Create a new itemListLock.
     *
     * @param itemListLockDTO the itemListLockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemListLockDTO, or with status {@code 400 (Bad Request)} if the itemListLock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ItemListLockDTO> createItemListLock(@Valid @RequestBody ItemListLockDTO itemListLockDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ItemListLock : {}", itemListLockDTO);
        if (itemListLockDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemListLock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        itemListLockDTO = this.itemListLockService.save(itemListLockDTO);
        return ResponseEntity.created(new URI("/api/item-list-locks/" + itemListLockDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, itemListLockDTO.getId().toString()))
            .body(itemListLockDTO);
    }

    /**
     * {@code PUT  /item-list-locks/:id} : Updates an existing itemListLock.
     *
     * @param id              the id of the itemListLockDTO to save.
     * @param itemListLockDTO the itemListLockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemListLockDTO,
     * or with status {@code 400 (Bad Request)} if the itemListLockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemListLockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemListLockDTO> updateItemListLock(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemListLockDTO itemListLockDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ItemListLock : {}, {}", id, itemListLockDTO);
        if (itemListLockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemListLockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemListLockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        itemListLockDTO = this.itemListLockService.update(itemListLockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemListLockDTO.getId().toString()))
            .body(itemListLockDTO);
    }

    /**
     * {@code PATCH  /item-list-locks/:id} : Partial updates given fields of an existing itemListLock, field will ignore if it is null
     *
     * @param id              the id of the itemListLockDTO to save.
     * @param itemListLockDTO the itemListLockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemListLockDTO,
     * or with status {@code 400 (Bad Request)} if the itemListLockDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemListLockDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemListLockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    public ResponseEntity<ItemListLockDTO> partialUpdateItemListLock(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemListLockDTO itemListLockDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ItemListLock partially : {}, {}", id, itemListLockDTO);
        if (itemListLockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemListLockDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemListLockRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemListLockDTO> result = this.itemListLockService.partialUpdate(itemListLockDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemListLockDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-list-locks} : get all the itemListLocks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemListLocks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ItemListLockDTO>> getAllItemListLocks(@ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ItemListLocks");
        Page<ItemListLockDTO> page = this.itemListLockService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-list-locks/:id} : get the "id" itemListLock.
     *
     * @param id the id of the itemListLockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemListLockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemListLockDTO> getItemListLock(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ItemListLock : {}", id);
        Optional<ItemListLockDTO> itemListLockDTO = this.itemListLockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemListLockDTO);
    }

    /**
     * {@code GET  /item-list-locks/check-availability} : Checked if there is enough stock and update lock if true.
     *
     * @param userUuid Unique universal id of the user - generated by the front end
     * @param tireId   technical id of the tire
     * @param quantity new item quantity
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the result of the check in body.
     */
    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkAvailability(
        @RequestParam String userUuid,
        @RequestParam Long tireId,
        @RequestParam Integer quantity
    ) {
        LOG.debug("REST request to check the item availability");
        if (!UUID_REGEX.matcher(userUuid).matches()) {
            throw new BadRequestAlertException("Bad UUID format", "", "");
        }
        if (quantity < 0) {
            throw new BadRequestAlertException("Quantity can't be negative", "", "");
        }
        Boolean hasLock = this.itemListLockService.isUserUuidAssociatedToAnExistingLock(UUID.fromString(userUuid));
        int attempt = 0;
        while (attempt < 5)
            try {
                Boolean result = this.itemListLockService.isItemAvailable(UUID.fromString(userUuid), tireId, quantity, hasLock);
                return ResponseEntity.ok().body(result);
            } catch (OptimisticLockException e) {
                attempt++;
            }
        return ResponseEntity.internalServerError().build();
    }

    /**
     * {@code GET  /item-list-locks/exist} : Checked if the user has a lock.
     *
     * @param userUuid Unique universal id of the user - generated by the front end
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the result of the check in body.
     */
    @GetMapping("/exist")
    public ResponseEntity<Boolean> isUserUuidAssociatedToAnExistingLock(@RequestParam String userUuid) {
        LOG.debug("REST request to check the item availability");
        if (!UUID_REGEX.matcher(userUuid).matches()) {
            throw new BadRequestAlertException("Bad UUID format", "", "");
        }
        return ResponseEntity.ok()
            .body(this.itemListLockService.isUserUuidAssociatedToAnExistingLock(UUID.fromString(userUuid)));
    }

    /**
     * {@code DELETE  /item-list-locks/delete-all} : delete all lock associated to the user.
     *
     * @param userUuid Unique universal id of the user - generated by the front end
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}
     */
    @DeleteMapping("/delete-all")
    public ResponseEntity<Boolean> emptyLock(@RequestParam String userUuid) {
        LOG.debug("REST request to check the item availability");
        if (!UUID_REGEX.matcher(userUuid).matches()) {
            throw new BadRequestAlertException("Bad UUID format", "", "");
        }
        int attempt = 0;
        while (attempt < 5)
            try {
                this.itemListLockService.deleteAllByUserUuid(UUID.fromString(userUuid));
                return ResponseEntity.noContent().build();
            } catch (OptimisticLockException e) {
                attempt++;
            }
        return ResponseEntity.internalServerError().build();
    }

    /**
     * {@code GET  /item-list-locks/by-user} : Get all lock by UserUuid
     *
     * @param userUuid Unique universal id of the user - generated by the front end
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ItemListLockDTO in body.
     */
    @GetMapping("/by-user")
    public ResponseEntity<List<ItemListLockDTO>> getAllByUserUuid(@RequestParam String userUuid) {
        LOG.debug("REST request to check the item availability");
        if (!UUID_REGEX.matcher(userUuid).matches()) {
            throw new BadRequestAlertException("Bad UUID format", "", "");
        }
        return ResponseEntity.ok()
            .body(this.itemListLockService.findAllItemListLockByUserUuid(UUID.fromString(userUuid)));
    }

    /**
     * {@code DELETE  /item-list-locks/:id} : delete the "id" itemListLock.
     *
     * @param id the id of the itemListLockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemListLock(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ItemListLock : {}", id);
        itemListLockService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

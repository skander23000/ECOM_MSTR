package fr.ecom.mstr.tire.web.rest;

import fr.ecom.mstr.tire.repository.TireRepository;
import fr.ecom.mstr.tire.service.TireService;
import fr.ecom.mstr.tire.service.dto.TireDTO;
import fr.ecom.mstr.tire.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * REST controller for managing {@link fr.ecom.mstr.tire.domain.Tire}.
 */
@RestController
@RequestMapping("/api/tires")
public class TireResource {

    private static final Logger LOG = LoggerFactory.getLogger(TireResource.class);

    private static final String ENTITY_NAME = "tire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TireService tireService;

    private final TireRepository tireRepository;

    public TireResource(TireService tireService, TireRepository tireRepository) {
        this.tireService = tireService;
        this.tireRepository = tireRepository;
    }

    /**
     * {@code POST  /tires} : Create a new tire.
     *
     * @param tireDTO the tireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tireDTO, or with status {@code 400 (Bad Request)} if the tire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TireDTO> createTire(@Valid @RequestBody TireDTO tireDTO) throws URISyntaxException {
        LOG.debug("REST request to save Tire : {}", tireDTO);
        if (tireDTO.getId() != null) {
            throw new BadRequestAlertException("A new tire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tireDTO = tireService.save(tireDTO);
        return ResponseEntity.created(new URI("/api/tires/" + tireDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tireDTO.getId().toString()))
            .body(tireDTO);
    }

    /**
     * {@code PUT  /tires/:id} : Updates an existing tire.
     *
     * @param id the id of the tireDTO to save.
     * @param tireDTO the tireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tireDTO,
     * or with status {@code 400 (Bad Request)} if the tireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TireDTO> updateTire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TireDTO tireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Tire : {}, {}", id, tireDTO);
        if (tireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tireDTO = tireService.update(tireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tireDTO.getId().toString()))
            .body(tireDTO);
    }

    /**
     * {@code PATCH  /tires/:id} : Partial updates given fields of an existing tire, field will ignore if it is null
     *
     * @param id the id of the tireDTO to save.
     * @param tireDTO the tireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tireDTO,
     * or with status {@code 400 (Bad Request)} if the tireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TireDTO> partialUpdateTire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TireDTO tireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Tire partially : {}, {}", id, tireDTO);
        if (tireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TireDTO> result = tireService.partialUpdate(tireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tires} : get all the tires.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tires in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TireDTO>> getAllTires(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Tires");
        Page<TireDTO> page;
        if (eagerload) {
            page = tireService.findAllWithEagerRelationships(pageable);
        } else {
            page = tireService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tires/:id} : get the "id" tire.
     *
     * @param id the id of the tireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TireDTO> getTire(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Tire : {}", id);
        Optional<TireDTO> tireDTO = tireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tireDTO);
    }

    /**
     * {@code DELETE  /tires/:id} : delete the "id" tire.
     *
     * @param id the id of the tireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTire(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Tire : {}", id);
        tireService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

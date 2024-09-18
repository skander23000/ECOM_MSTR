package fr.ecom.mstr.tire.web.rest;

import fr.ecom.mstr.tire.repository.TireBrandRepository;
import fr.ecom.mstr.tire.security.AuthoritiesConstants;
import fr.ecom.mstr.tire.service.TireBrandService;
import fr.ecom.mstr.tire.service.dto.TireBrandDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.ecom.mstr.tire.domain.TireBrand}.
 */
@RestController
@RequestMapping("/api/tire-brands")
public class TireBrandResource {

    private static final Logger LOG = LoggerFactory.getLogger(TireBrandResource.class);

    private static final String ENTITY_NAME = "tireBrand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TireBrandService tireBrandService;

    private final TireBrandRepository tireBrandRepository;

    public TireBrandResource(TireBrandService tireBrandService, TireBrandRepository tireBrandRepository) {
        this.tireBrandService = tireBrandService;
        this.tireBrandRepository = tireBrandRepository;
    }

    /**
     * {@code POST  /tire-brands} : Create a new tireBrand.
     *
     * @param tireBrandDTO the tireBrandDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tireBrandDTO, or with status {@code 400 (Bad Request)} if the tireBrand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<TireBrandDTO> createTireBrand(@Valid @RequestBody TireBrandDTO tireBrandDTO) throws URISyntaxException {
        LOG.debug("REST request to save TireBrand : {}", tireBrandDTO);
        if (tireBrandDTO.getId() != null) {
            throw new BadRequestAlertException("A new tireBrand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tireBrandDTO = tireBrandService.save(tireBrandDTO);
        return ResponseEntity.created(new URI("/api/tire-brands/" + tireBrandDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tireBrandDTO.getId().toString()))
            .body(tireBrandDTO);
    }

    /**
     * {@code PUT  /tire-brands/:id} : Updates an existing tireBrand.
     *
     * @param id the id of the tireBrandDTO to save.
     * @param tireBrandDTO the tireBrandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tireBrandDTO,
     * or with status {@code 400 (Bad Request)} if the tireBrandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tireBrandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<TireBrandDTO> updateTireBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TireBrandDTO tireBrandDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TireBrand : {}, {}", id, tireBrandDTO);
        if (tireBrandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tireBrandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tireBrandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tireBrandDTO = tireBrandService.update(tireBrandDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tireBrandDTO.getId().toString()))
            .body(tireBrandDTO);
    }

    /**
     * {@code PATCH  /tire-brands/:id} : Partial updates given fields of an existing tireBrand, field will ignore if it is null
     *
     * @param id the id of the tireBrandDTO to save.
     * @param tireBrandDTO the tireBrandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tireBrandDTO,
     * or with status {@code 400 (Bad Request)} if the tireBrandDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tireBrandDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tireBrandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<TireBrandDTO> partialUpdateTireBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TireBrandDTO tireBrandDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TireBrand partially : {}, {}", id, tireBrandDTO);
        if (tireBrandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tireBrandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tireBrandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TireBrandDTO> result = tireBrandService.partialUpdate(tireBrandDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tireBrandDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tire-brands} : get all the tireBrands.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tireBrands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TireBrandDTO>> getAllTireBrands(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of TireBrands");
        Page<TireBrandDTO> page = tireBrandService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tire-brands/:id} : get the "id" tireBrand.
     *
     * @param id the id of the tireBrandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tireBrandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TireBrandDTO> getTireBrand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TireBrand : {}", id);
        Optional<TireBrandDTO> tireBrandDTO = tireBrandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tireBrandDTO);
    }

    /**
     * {@code DELETE  /tire-brands/:id} : delete the "id" tireBrand.
     *
     * @param id the id of the tireBrandDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteTireBrand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TireBrand : {}", id);
        tireBrandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

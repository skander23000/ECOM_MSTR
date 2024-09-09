package fr.ecom.mstr.tire.web.rest;

import static fr.ecom.mstr.tire.domain.TireBrandAsserts.*;
import static fr.ecom.mstr.tire.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ecom.mstr.tire.IntegrationTest;
import fr.ecom.mstr.tire.domain.TireBrand;
import fr.ecom.mstr.tire.repository.TireBrandRepository;
import fr.ecom.mstr.tire.service.dto.TireBrandDTO;
import fr.ecom.mstr.tire.service.mapper.TireBrandMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TireBrandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TireBrandResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_URL = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tire-brands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TireBrandRepository tireBrandRepository;

    @Autowired
    private TireBrandMapper tireBrandMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTireBrandMockMvc;

    private TireBrand tireBrand;

    private TireBrand insertedTireBrand;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TireBrand createEntity() {
        return new TireBrand().name(DEFAULT_NAME).logoUrl(DEFAULT_LOGO_URL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TireBrand createUpdatedEntity() {
        return new TireBrand().name(UPDATED_NAME).logoUrl(UPDATED_LOGO_URL);
    }

    @BeforeEach
    public void initTest() {
        tireBrand = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTireBrand != null) {
            tireBrandRepository.delete(insertedTireBrand);
            insertedTireBrand = null;
        }
    }

    @Test
    @Transactional
    void createTireBrand() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TireBrand
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(tireBrand);
        var returnedTireBrandDTO = om.readValue(
            restTireBrandMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireBrandDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TireBrandDTO.class
        );

        // Validate the TireBrand in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTireBrand = tireBrandMapper.toEntity(returnedTireBrandDTO);
        assertTireBrandUpdatableFieldsEquals(returnedTireBrand, getPersistedTireBrand(returnedTireBrand));

        insertedTireBrand = returnedTireBrand;
    }

    @Test
    @Transactional
    void createTireBrandWithExistingId() throws Exception {
        // Create the TireBrand with an existing ID
        tireBrand.setId(1L);
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(tireBrand);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTireBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireBrandDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TireBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tireBrand.setName(null);

        // Create the TireBrand, which fails.
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(tireBrand);

        restTireBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireBrandDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLogoUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tireBrand.setLogoUrl(null);

        // Create the TireBrand, which fails.
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(tireBrand);

        restTireBrandMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireBrandDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTireBrands() throws Exception {
        // Initialize the database
        insertedTireBrand = tireBrandRepository.saveAndFlush(tireBrand);

        // Get all the tireBrandList
        restTireBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tireBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].logoUrl").value(hasItem(DEFAULT_LOGO_URL)));
    }

    @Test
    @Transactional
    void getTireBrand() throws Exception {
        // Initialize the database
        insertedTireBrand = tireBrandRepository.saveAndFlush(tireBrand);

        // Get the tireBrand
        restTireBrandMockMvc
            .perform(get(ENTITY_API_URL_ID, tireBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tireBrand.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.logoUrl").value(DEFAULT_LOGO_URL));
    }

    @Test
    @Transactional
    void getNonExistingTireBrand() throws Exception {
        // Get the tireBrand
        restTireBrandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTireBrand() throws Exception {
        // Initialize the database
        insertedTireBrand = tireBrandRepository.saveAndFlush(tireBrand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tireBrand
        TireBrand updatedTireBrand = tireBrandRepository.findById(tireBrand.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTireBrand are not directly saved in db
        em.detach(updatedTireBrand);
        updatedTireBrand.name(UPDATED_NAME).logoUrl(UPDATED_LOGO_URL);
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(updatedTireBrand);

        restTireBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tireBrandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tireBrandDTO))
            )
            .andExpect(status().isOk());

        // Validate the TireBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTireBrandToMatchAllProperties(updatedTireBrand);
    }

    @Test
    @Transactional
    void putNonExistingTireBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tireBrand.setId(longCount.incrementAndGet());

        // Create the TireBrand
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(tireBrand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTireBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tireBrandDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tireBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TireBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTireBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tireBrand.setId(longCount.incrementAndGet());

        // Create the TireBrand
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(tireBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTireBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tireBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TireBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTireBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tireBrand.setId(longCount.incrementAndGet());

        // Create the TireBrand
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(tireBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTireBrandMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireBrandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TireBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTireBrandWithPatch() throws Exception {
        // Initialize the database
        insertedTireBrand = tireBrandRepository.saveAndFlush(tireBrand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tireBrand using partial update
        TireBrand partialUpdatedTireBrand = new TireBrand();
        partialUpdatedTireBrand.setId(tireBrand.getId());

        restTireBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTireBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTireBrand))
            )
            .andExpect(status().isOk());

        // Validate the TireBrand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTireBrandUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTireBrand, tireBrand),
            getPersistedTireBrand(tireBrand)
        );
    }

    @Test
    @Transactional
    void fullUpdateTireBrandWithPatch() throws Exception {
        // Initialize the database
        insertedTireBrand = tireBrandRepository.saveAndFlush(tireBrand);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tireBrand using partial update
        TireBrand partialUpdatedTireBrand = new TireBrand();
        partialUpdatedTireBrand.setId(tireBrand.getId());

        partialUpdatedTireBrand.name(UPDATED_NAME).logoUrl(UPDATED_LOGO_URL);

        restTireBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTireBrand.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTireBrand))
            )
            .andExpect(status().isOk());

        // Validate the TireBrand in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTireBrandUpdatableFieldsEquals(partialUpdatedTireBrand, getPersistedTireBrand(partialUpdatedTireBrand));
    }

    @Test
    @Transactional
    void patchNonExistingTireBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tireBrand.setId(longCount.incrementAndGet());

        // Create the TireBrand
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(tireBrand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTireBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tireBrandDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tireBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TireBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTireBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tireBrand.setId(longCount.incrementAndGet());

        // Create the TireBrand
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(tireBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTireBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tireBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TireBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTireBrand() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tireBrand.setId(longCount.incrementAndGet());

        // Create the TireBrand
        TireBrandDTO tireBrandDTO = tireBrandMapper.toDto(tireBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTireBrandMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tireBrandDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TireBrand in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTireBrand() throws Exception {
        // Initialize the database
        insertedTireBrand = tireBrandRepository.saveAndFlush(tireBrand);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tireBrand
        restTireBrandMockMvc
            .perform(delete(ENTITY_API_URL_ID, tireBrand.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tireBrandRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected TireBrand getPersistedTireBrand(TireBrand tireBrand) {
        return tireBrandRepository.findById(tireBrand.getId()).orElseThrow();
    }

    protected void assertPersistedTireBrandToMatchAllProperties(TireBrand expectedTireBrand) {
        assertTireBrandAllPropertiesEquals(expectedTireBrand, getPersistedTireBrand(expectedTireBrand));
    }

    protected void assertPersistedTireBrandToMatchUpdatableProperties(TireBrand expectedTireBrand) {
        assertTireBrandAllUpdatablePropertiesEquals(expectedTireBrand, getPersistedTireBrand(expectedTireBrand));
    }
}

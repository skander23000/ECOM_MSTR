package fr.ecom.mstr.tire.web.rest;

import static fr.ecom.mstr.tire.domain.TireAsserts.*;
import static fr.ecom.mstr.tire.web.rest.TestUtil.createUpdateProxyForBean;
import static fr.ecom.mstr.tire.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ecom.mstr.tire.IntegrationTest;
import fr.ecom.mstr.tire.domain.Tire;
import fr.ecom.mstr.tire.domain.enumeration.ChargeIndex;
import fr.ecom.mstr.tire.domain.enumeration.SpeedIndex;
import fr.ecom.mstr.tire.domain.enumeration.TireType;
import fr.ecom.mstr.tire.repository.TireRepository;
import fr.ecom.mstr.tire.service.TireService;
import fr.ecom.mstr.tire.service.dto.TireDTO;
import fr.ecom.mstr.tire.service.mapper.TireMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TireResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TireResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String DEFAULT_TIRE_WIDTH = "AAAAAAAAAA";
    private static final String UPDATED_TIRE_WIDTH = "BBBBBBBBBB";

    private static final String DEFAULT_TIRE_HEIGHT = "AAAAAAAAAA";
    private static final String UPDATED_TIRE_HEIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_TIRE_DIAMETER = "AAAAAAAAAA";
    private static final String UPDATED_TIRE_DIAMETER = "BBBBBBBBBB";

    private static final TireType DEFAULT_TIRE_TYPE = TireType.SUMMER;
    private static final TireType UPDATED_TIRE_TYPE = TireType.WINTER;

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final SpeedIndex DEFAULT_SPEED_INDEX = SpeedIndex.A_ONE;
    private static final SpeedIndex UPDATED_SPEED_INDEX = SpeedIndex.A_EIGHT;

    private static final ChargeIndex DEFAULT_WEIGHT_INDEX = ChargeIndex.TWENTY;
    private static final ChargeIndex UPDATED_WEIGHT_INDEX = ChargeIndex.SIXTY;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Boolean DEFAULT_DISABLE = false;
    private static final Boolean UPDATED_DISABLE = true;

    private static final String DEFAULT_DISABLE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_DISABLE_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TireRepository tireRepository;

    @Mock
    private TireRepository tireRepositoryMock;

    @Autowired
    private TireMapper tireMapper;

    @Mock
    private TireService tireServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTireMockMvc;

    private Tire tire;

    private Tire insertedTire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tire createEntity() {
        return new Tire()
            .reference(DEFAULT_REFERENCE)
            .name(DEFAULT_NAME)
            .price(DEFAULT_PRICE)
            .tireWidth(DEFAULT_TIRE_WIDTH)
            .tireHeight(DEFAULT_TIRE_HEIGHT)
            .tireDiameter(DEFAULT_TIRE_DIAMETER)
            .tireType(DEFAULT_TIRE_TYPE)
            .imageUrl(DEFAULT_IMAGE_URL)
            .speedIndex(DEFAULT_SPEED_INDEX)
            .weightIndex(DEFAULT_WEIGHT_INDEX)
            .quantity(DEFAULT_QUANTITY)
            .disable(DEFAULT_DISABLE)
            .disableReason(DEFAULT_DISABLE_REASON)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tire createUpdatedEntity() {
        return new Tire()
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .tireWidth(UPDATED_TIRE_WIDTH)
            .tireHeight(UPDATED_TIRE_HEIGHT)
            .tireDiameter(UPDATED_TIRE_DIAMETER)
            .tireType(UPDATED_TIRE_TYPE)
            .imageUrl(UPDATED_IMAGE_URL)
            .speedIndex(UPDATED_SPEED_INDEX)
            .weightIndex(UPDATED_WEIGHT_INDEX)
            .quantity(UPDATED_QUANTITY)
            .disable(UPDATED_DISABLE)
            .disableReason(UPDATED_DISABLE_REASON)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        tire = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTire != null) {
            tireRepository.delete(insertedTire);
            insertedTire = null;
        }
    }

    @Test
    @Transactional
    void createTire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tire
        TireDTO tireDTO = tireMapper.toDto(tire);
        var returnedTireDTO = om.readValue(
            restTireMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TireDTO.class
        );

        // Validate the Tire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTire = tireMapper.toEntity(returnedTireDTO);
        assertTireUpdatableFieldsEquals(returnedTire, getPersistedTire(returnedTire));

        insertedTire = returnedTire;
    }

    @Test
    @Transactional
    void createTireWithExistingId() throws Exception {
        // Create the Tire with an existing ID
        tire.setId(1L);
        TireDTO tireDTO = tireMapper.toDto(tire);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setName(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setPrice(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTireWidthIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setTireWidth(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTireHeightIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setTireHeight(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTireDiameterIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setTireDiameter(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTireTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setTireType(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImageUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setImageUrl(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSpeedIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setSpeedIndex(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setWeightIndex(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setQuantity(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDisableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tire.setDisable(null);

        // Create the Tire, which fails.
        TireDTO tireDTO = tireMapper.toDto(tire);

        restTireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTires() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList
        restTireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tire.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].tireWidth").value(hasItem(DEFAULT_TIRE_WIDTH)))
            .andExpect(jsonPath("$.[*].tireHeight").value(hasItem(DEFAULT_TIRE_HEIGHT)))
            .andExpect(jsonPath("$.[*].tireDiameter").value(hasItem(DEFAULT_TIRE_DIAMETER)))
            .andExpect(jsonPath("$.[*].tireType").value(hasItem(DEFAULT_TIRE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].speedIndex").value(hasItem(DEFAULT_SPEED_INDEX.toString())))
            .andExpect(jsonPath("$.[*].weightIndex").value(hasItem(DEFAULT_WEIGHT_INDEX.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].disable").value(hasItem(DEFAULT_DISABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].disableReason").value(hasItem(DEFAULT_DISABLE_REASON)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTiresWithEagerRelationshipsIsEnabled() throws Exception {
        when(tireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTiresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tireRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTire() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get the tire
        restTireMockMvc
            .perform(get(ENTITY_API_URL_ID, tire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tire.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.tireWidth").value(DEFAULT_TIRE_WIDTH))
            .andExpect(jsonPath("$.tireHeight").value(DEFAULT_TIRE_HEIGHT))
            .andExpect(jsonPath("$.tireDiameter").value(DEFAULT_TIRE_DIAMETER))
            .andExpect(jsonPath("$.tireType").value(DEFAULT_TIRE_TYPE.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.speedIndex").value(DEFAULT_SPEED_INDEX.toString()))
            .andExpect(jsonPath("$.weightIndex").value(DEFAULT_WEIGHT_INDEX.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.disable").value(DEFAULT_DISABLE.booleanValue()))
            .andExpect(jsonPath("$.disableReason").value(DEFAULT_DISABLE_REASON))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingTire() throws Exception {
        // Get the tire
        restTireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTire() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tire
        Tire updatedTire = tireRepository.findById(tire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTire are not directly saved in db
        em.detach(updatedTire);
        updatedTire
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .tireWidth(UPDATED_TIRE_WIDTH)
            .tireHeight(UPDATED_TIRE_HEIGHT)
            .tireDiameter(UPDATED_TIRE_DIAMETER)
            .tireType(UPDATED_TIRE_TYPE)
            .imageUrl(UPDATED_IMAGE_URL)
            .speedIndex(UPDATED_SPEED_INDEX)
            .weightIndex(UPDATED_WEIGHT_INDEX)
            .quantity(UPDATED_QUANTITY)
            .disable(UPDATED_DISABLE)
            .disableReason(UPDATED_DISABLE_REASON)
            .description(UPDATED_DESCRIPTION);
        TireDTO tireDTO = tireMapper.toDto(updatedTire);

        restTireMockMvc
            .perform(put(ENTITY_API_URL_ID, tireDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isOk());

        // Validate the Tire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTireToMatchAllProperties(updatedTire);
    }

    @Test
    @Transactional
    void putNonExistingTire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tire.setId(longCount.incrementAndGet());

        // Create the Tire
        TireDTO tireDTO = tireMapper.toDto(tire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTireMockMvc
            .perform(put(ENTITY_API_URL_ID, tireDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tire.setId(longCount.incrementAndGet());

        // Create the Tire
        TireDTO tireDTO = tireMapper.toDto(tire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tire.setId(longCount.incrementAndGet());

        // Create the Tire
        TireDTO tireDTO = tireMapper.toDto(tire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTireWithPatch() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tire using partial update
        Tire partialUpdatedTire = new Tire();
        partialUpdatedTire.setId(tire.getId());

        partialUpdatedTire
            .price(UPDATED_PRICE)
            .tireType(UPDATED_TIRE_TYPE)
            .speedIndex(UPDATED_SPEED_INDEX)
            .weightIndex(UPDATED_WEIGHT_INDEX)
            .disable(UPDATED_DISABLE)
            .disableReason(UPDATED_DISABLE_REASON)
            .description(UPDATED_DESCRIPTION);

        restTireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTire))
            )
            .andExpect(status().isOk());

        // Validate the Tire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTireUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTire, tire), getPersistedTire(tire));
    }

    @Test
    @Transactional
    void fullUpdateTireWithPatch() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tire using partial update
        Tire partialUpdatedTire = new Tire();
        partialUpdatedTire.setId(tire.getId());

        partialUpdatedTire
            .reference(UPDATED_REFERENCE)
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .tireWidth(UPDATED_TIRE_WIDTH)
            .tireHeight(UPDATED_TIRE_HEIGHT)
            .tireDiameter(UPDATED_TIRE_DIAMETER)
            .tireType(UPDATED_TIRE_TYPE)
            .imageUrl(UPDATED_IMAGE_URL)
            .speedIndex(UPDATED_SPEED_INDEX)
            .weightIndex(UPDATED_WEIGHT_INDEX)
            .quantity(UPDATED_QUANTITY)
            .disable(UPDATED_DISABLE)
            .disableReason(UPDATED_DISABLE_REASON)
            .description(UPDATED_DESCRIPTION);

        restTireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTire))
            )
            .andExpect(status().isOk());

        // Validate the Tire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTireUpdatableFieldsEquals(partialUpdatedTire, getPersistedTire(partialUpdatedTire));
    }

    @Test
    @Transactional
    void patchNonExistingTire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tire.setId(longCount.incrementAndGet());

        // Create the Tire
        TireDTO tireDTO = tireMapper.toDto(tire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tireDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tire.setId(longCount.incrementAndGet());

        // Create the Tire
        TireDTO tireDTO = tireMapper.toDto(tire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tire.setId(longCount.incrementAndGet());

        // Create the Tire
        TireDTO tireDTO = tireMapper.toDto(tire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTireMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTire() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tire
        restTireMockMvc
            .perform(delete(ENTITY_API_URL_ID, tire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tireRepository.count();
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

    protected Tire getPersistedTire(Tire tire) {
        return tireRepository.findById(tire.getId()).orElseThrow();
    }

    protected void assertPersistedTireToMatchAllProperties(Tire expectedTire) {
        assertTireAllPropertiesEquals(expectedTire, getPersistedTire(expectedTire));
    }

    protected void assertPersistedTireToMatchUpdatableProperties(Tire expectedTire) {
        assertTireAllUpdatablePropertiesEquals(expectedTire, getPersistedTire(expectedTire));
    }
}

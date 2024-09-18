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
import fr.ecom.mstr.tire.domain.TireBrand;
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
    private static final BigDecimal SMALLER_PRICE = BigDecimal.ZERO;

    private static final BigDecimal DEFAULT_TIRE_WIDTH = new BigDecimal(1);
    private static final BigDecimal UPDATED_TIRE_WIDTH = new BigDecimal(2);
    private static final BigDecimal SMALLER_TIRE_WIDTH = BigDecimal.ZERO;

    private static final BigDecimal DEFAULT_TIRE_HEIGHT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TIRE_HEIGHT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TIRE_HEIGHT = BigDecimal.ZERO;

    private static final BigDecimal DEFAULT_TIRE_DIAMETER = new BigDecimal(1);
    private static final BigDecimal UPDATED_TIRE_DIAMETER = new BigDecimal(2);
    private static final BigDecimal SMALLER_TIRE_DIAMETER = BigDecimal.ZERO;

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
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final Boolean DEFAULT_DISABLE = false;
    private static final Boolean UPDATED_DISABLE = true;

    private static final String DEFAULT_DISABLE_REASON = "AAAAAAAAAA";
    private static final String UPDATED_DISABLE_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final Integer DEFAULT_VERSION = 1;

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
            .description(DEFAULT_DESCRIPTION)
            .version(DEFAULT_VERSION);
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
            .description(UPDATED_DESCRIPTION)
            .version(DEFAULT_VERSION);
    }

    @BeforeEach
    public void initTest() {
        tire = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTire != null) {
            tireRepository.deleteById(insertedTire.getId());
            insertedTire = null;
        }
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(insertedTire.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].tireWidth").value(hasItem(sameNumber(DEFAULT_TIRE_WIDTH))))
            .andExpect(jsonPath("$.[*].tireHeight").value(hasItem(sameNumber(DEFAULT_TIRE_HEIGHT))))
            .andExpect(jsonPath("$.[*].tireDiameter").value(hasItem(sameNumber(DEFAULT_TIRE_DIAMETER))))
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
            .perform(get(ENTITY_API_URL_ID, insertedTire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insertedTire.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.tireWidth").value(sameNumber(DEFAULT_TIRE_WIDTH)))
            .andExpect(jsonPath("$.tireHeight").value(sameNumber(DEFAULT_TIRE_HEIGHT)))
            .andExpect(jsonPath("$.tireDiameter").value(sameNumber(DEFAULT_TIRE_DIAMETER)))
            .andExpect(jsonPath("$.tireType").value(DEFAULT_TIRE_TYPE.toString()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.speedIndex").value(DEFAULT_SPEED_INDEX.toString()))
            .andExpect(jsonPath("$.weightIndex").value(DEFAULT_WEIGHT_INDEX.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.disable").value(DEFAULT_DISABLE.booleanValue()))
            .andExpect(jsonPath("$.disableReason").value(DEFAULT_DISABLE_REASON))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION));
    }

    @Test
    @Transactional
    void getTiresByIdFiltering() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        Long id = insertedTire.getId();

        defaultTireFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTireFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTireFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTiresByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where reference equals to
        defaultTireFiltering("reference.equals=" + DEFAULT_REFERENCE, "reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTiresByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where reference in
        defaultTireFiltering("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE, "reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTiresByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where reference is not null
        defaultTireFiltering("reference.specified=true", "reference.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByReferenceContainsSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where reference contains
        defaultTireFiltering("reference.contains=" + DEFAULT_REFERENCE, "reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTiresByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where reference does not contain
        defaultTireFiltering("reference.doesNotContain=" + UPDATED_REFERENCE, "reference.doesNotContain=" + DEFAULT_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTiresByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where name equals to
        defaultTireFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTiresByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where name in
        defaultTireFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTiresByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where name is not null
        defaultTireFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where name contains
        defaultTireFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTiresByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where name does not contain
        defaultTireFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTiresByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price equals to
        defaultTireFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllTiresByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price in
        defaultTireFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllTiresByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is not null
        defaultTireFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is greater than or equal to
        defaultTireFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllTiresByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is less than or equal to
        defaultTireFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllTiresByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is less than
        defaultTireFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllTiresByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is greater than
        defaultTireFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllTiresByTireWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireWidth equals to
        defaultTireFiltering("tireWidth.equals=" + DEFAULT_TIRE_WIDTH, "tireWidth.equals=" + UPDATED_TIRE_WIDTH);
    }

    @Test
    @Transactional
    void getAllTiresByTireWidthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireWidth in
        defaultTireFiltering("tireWidth.in=" + DEFAULT_TIRE_WIDTH + "," + UPDATED_TIRE_WIDTH, "tireWidth.in=" + UPDATED_TIRE_WIDTH);
    }

    @Test
    @Transactional
    void getAllTiresByTireWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireWidth is not null
        defaultTireFiltering("tireWidth.specified=true", "tireWidth.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByIsTireWidthGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is greater than or equal to
        defaultTireFiltering("tireWidth.greaterThanOrEqual=" + DEFAULT_TIRE_WIDTH, "tireWidth.greaterThanOrEqual=" + UPDATED_TIRE_WIDTH);
    }

    @Test
    @Transactional
    void getAllTiresByTireWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is less than or equal to
        defaultTireFiltering("tireWidth.lessThanOrEqual=" + DEFAULT_TIRE_WIDTH, "tireWidth.lessThanOrEqual=" + SMALLER_TIRE_WIDTH);
    }

    @Test
    @Transactional
    void getAllTiresByTireWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is less than
        defaultTireFiltering("tireWidth.lessThan=" + UPDATED_TIRE_WIDTH, "tireWidth.lessThan=" + DEFAULT_TIRE_WIDTH);
    }

    @Test
    @Transactional
    void getAllTiresByTireWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is greater than
        defaultTireFiltering("tireWidth.greaterThan=" + SMALLER_TIRE_WIDTH, "tireWidth.greaterThan=" + DEFAULT_TIRE_WIDTH);
    }

    @Test
    @Transactional
    void getAllTiresByTireHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireHeight equals to
        defaultTireFiltering("tireHeight.equals=" + DEFAULT_TIRE_HEIGHT, "tireHeight.equals=" + UPDATED_TIRE_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTiresByTireHeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireHeight in
        defaultTireFiltering("tireHeight.in=" + DEFAULT_TIRE_HEIGHT + "," + UPDATED_TIRE_HEIGHT, "tireHeight.in=" + UPDATED_TIRE_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTiresByTireHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireHeight is not null
        defaultTireFiltering("tireHeight.specified=true", "tireHeight.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByIsTireHeightGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is greater than or equal to
        defaultTireFiltering("tireHeight.greaterThanOrEqual=" + DEFAULT_TIRE_HEIGHT, "tireHeight.greaterThanOrEqual=" + UPDATED_TIRE_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTiresByTireHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is less than or equal to
        defaultTireFiltering("tireHeight.lessThanOrEqual=" + DEFAULT_TIRE_HEIGHT, "tireHeight.lessThanOrEqual=" + SMALLER_TIRE_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTiresByTireHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is less than
        defaultTireFiltering("tireHeight.lessThan=" + UPDATED_TIRE_HEIGHT, "tireHeight.lessThan=" + DEFAULT_TIRE_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTiresByTireHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is greater than
        defaultTireFiltering("tireHeight.greaterThan=" + SMALLER_TIRE_HEIGHT, "tireHeight.greaterThan=" + DEFAULT_TIRE_HEIGHT);
    }

    @Test
    @Transactional
    void getAllTiresByTireDiameterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireDiameter equals to
        defaultTireFiltering("tireDiameter.equals=" + DEFAULT_TIRE_DIAMETER, "tireDiameter.equals=" + UPDATED_TIRE_DIAMETER);
    }

    @Test
    @Transactional
    void getAllTiresByTireDiameterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireDiameter in
        defaultTireFiltering(
            "tireDiameter.in=" + DEFAULT_TIRE_DIAMETER + "," + UPDATED_TIRE_DIAMETER,
            "tireDiameter.in=" + UPDATED_TIRE_DIAMETER
        );
    }

    @Test
    @Transactional
    void getAllTiresByTireDiameterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireDiameter is not null
        defaultTireFiltering("tireDiameter.specified=true", "tireDiameter.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByIsTireDiameterGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is greater than or equal to
        defaultTireFiltering("tireDiameter.greaterThanOrEqual=" + DEFAULT_TIRE_DIAMETER, "tireDiameter.greaterThanOrEqual=" + UPDATED_TIRE_DIAMETER);
    }

    @Test
    @Transactional
    void getAllTiresByTireDiameterIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is less than or equal to
        defaultTireFiltering("tireDiameter.lessThanOrEqual=" + DEFAULT_TIRE_DIAMETER, "tireDiameter.lessThanOrEqual=" + SMALLER_TIRE_DIAMETER);
    }

    @Test
    @Transactional
    void getAllTiresByTireDiameterIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is less than
        defaultTireFiltering("tireDiameter.lessThan=" + UPDATED_TIRE_DIAMETER, "tireDiameter.lessThan=" + DEFAULT_TIRE_DIAMETER);
    }

    @Test
    @Transactional
    void getAllTiresByTireDiameterIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where price is greater than
        defaultTireFiltering("tireDiameter.greaterThan=" + SMALLER_TIRE_DIAMETER, "tireDiameter.greaterThan=" + DEFAULT_TIRE_DIAMETER);
    }

    @Test
    @Transactional
    void getAllTiresByTireTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireType equals to
        defaultTireFiltering("tireType.equals=" + DEFAULT_TIRE_TYPE, "tireType.equals=" + UPDATED_TIRE_TYPE);
    }

    @Test
    @Transactional
    void getAllTiresByTireTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireType in
        defaultTireFiltering("tireType.in=" + DEFAULT_TIRE_TYPE + "," + UPDATED_TIRE_TYPE, "tireType.in=" + UPDATED_TIRE_TYPE);
    }

    @Test
    @Transactional
    void getAllTiresByTireTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where tireType is not null
        defaultTireFiltering("tireType.specified=true", "tireType.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where imageUrl equals to
        defaultTireFiltering("imageUrl.equals=" + DEFAULT_IMAGE_URL, "imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllTiresByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where imageUrl in
        defaultTireFiltering("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL, "imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllTiresByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where imageUrl is not null
        defaultTireFiltering("imageUrl.specified=true", "imageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where imageUrl contains
        defaultTireFiltering("imageUrl.contains=" + DEFAULT_IMAGE_URL, "imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllTiresByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where imageUrl does not contain
        defaultTireFiltering("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL, "imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllTiresBySpeedIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where speedIndex equals to
        defaultTireFiltering("speedIndex.equals=" + DEFAULT_SPEED_INDEX, "speedIndex.equals=" + UPDATED_SPEED_INDEX);
    }

    @Test
    @Transactional
    void getAllTiresBySpeedIndexIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where speedIndex in
        defaultTireFiltering("speedIndex.in=" + DEFAULT_SPEED_INDEX + "," + UPDATED_SPEED_INDEX, "speedIndex.in=" + UPDATED_SPEED_INDEX);
    }

    @Test
    @Transactional
    void getAllTiresBySpeedIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where speedIndex is not null
        defaultTireFiltering("speedIndex.specified=true", "speedIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByWeightIndexIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where weightIndex equals to
        defaultTireFiltering("weightIndex.equals=" + DEFAULT_WEIGHT_INDEX, "weightIndex.equals=" + UPDATED_WEIGHT_INDEX);
    }

    @Test
    @Transactional
    void getAllTiresByWeightIndexIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where weightIndex in
        defaultTireFiltering(
            "weightIndex.in=" + DEFAULT_WEIGHT_INDEX + "," + UPDATED_WEIGHT_INDEX,
            "weightIndex.in=" + UPDATED_WEIGHT_INDEX
        );
    }

    @Test
    @Transactional
    void getAllTiresByWeightIndexIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where weightIndex is not null
        defaultTireFiltering("weightIndex.specified=true", "weightIndex.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where quantity equals to
        defaultTireFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTiresByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where quantity in
        defaultTireFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTiresByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where quantity is not null
        defaultTireFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where quantity is greater than or equal to
        defaultTireFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTiresByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where quantity is less than or equal to
        defaultTireFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTiresByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where quantity is less than
        defaultTireFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTiresByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where quantity is greater than
        defaultTireFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllTiresByDisableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where disable equals to
        defaultTireFiltering("disable.equals=" + DEFAULT_DISABLE, "disable.equals=" + UPDATED_DISABLE);
    }

    @Test
    @Transactional
    void getAllTiresByDisableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where disable in
        defaultTireFiltering("disable.in=" + DEFAULT_DISABLE + "," + UPDATED_DISABLE, "disable.in=" + UPDATED_DISABLE);
    }

    @Test
    @Transactional
    void getAllTiresByDisableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where disable is not null
        defaultTireFiltering("disable.specified=true", "disable.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByDisableReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where disableReason equals to
        defaultTireFiltering("disableReason.equals=" + DEFAULT_DISABLE_REASON, "disableReason.equals=" + UPDATED_DISABLE_REASON);
    }

    @Test
    @Transactional
    void getAllTiresByDisableReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where disableReason in
        defaultTireFiltering(
            "disableReason.in=" + DEFAULT_DISABLE_REASON + "," + UPDATED_DISABLE_REASON,
            "disableReason.in=" + UPDATED_DISABLE_REASON
        );
    }

    @Test
    @Transactional
    void getAllTiresByDisableReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where disableReason is not null
        defaultTireFiltering("disableReason.specified=true", "disableReason.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByDisableReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where disableReason contains
        defaultTireFiltering("disableReason.contains=" + DEFAULT_DISABLE_REASON, "disableReason.contains=" + UPDATED_DISABLE_REASON);
    }

    @Test
    @Transactional
    void getAllTiresByDisableReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where disableReason does not contain
        defaultTireFiltering(
            "disableReason.doesNotContain=" + UPDATED_DISABLE_REASON,
            "disableReason.doesNotContain=" + DEFAULT_DISABLE_REASON
        );
    }

    @Test
    @Transactional
    void getAllTiresByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where description equals to
        defaultTireFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTiresByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where description in
        defaultTireFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTiresByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where description is not null
        defaultTireFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTiresByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where description contains
        defaultTireFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTiresByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        // Get all the tireList where description does not contain
        defaultTireFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTiresByTireBrandIsEqualToSomething() throws Exception {
        TireBrand tireBrand;
        if (TestUtil.findAll(em, TireBrand.class).isEmpty()) {
            insertedTire = tireRepository.saveAndFlush(tire);
            tireBrand = TireBrandResourceIT.createEntity();
        } else {
            tireBrand = TestUtil.findAll(em, TireBrand.class).get(0);
        }
        em.persist(tireBrand);
        em.flush();
        insertedTire.setTireBrand(tireBrand);
        insertedTire = tireRepository.saveAndFlush(insertedTire);
        Long tireBrandId = tireBrand.getId();
        // Get all the tireList where tireBrand equals to tireBrandId
        defaultTireShouldBeFound("tireBrandId.equals=" + tireBrandId);

        // Get all the tireList where tireBrand equals to (tireBrandId + 1)
        defaultTireShouldNotBeFound("tireBrandId.equals=" + (tireBrandId + 1));
    }

    private void defaultTireFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTireShouldBeFound(shouldBeFound);
        defaultTireShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTireShouldBeFound(String filter) throws Exception {
        restTireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insertedTire.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].tireWidth").value(hasItem(sameNumber(DEFAULT_TIRE_WIDTH))))
            .andExpect(jsonPath("$.[*].tireHeight").value(hasItem(sameNumber(DEFAULT_TIRE_HEIGHT))))
            .andExpect(jsonPath("$.[*].tireDiameter").value(hasItem(sameNumber(DEFAULT_TIRE_DIAMETER))))
            .andExpect(jsonPath("$.[*].tireType").value(hasItem(DEFAULT_TIRE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].speedIndex").value(hasItem(DEFAULT_SPEED_INDEX.toString())))
            .andExpect(jsonPath("$.[*].weightIndex").value(hasItem(DEFAULT_WEIGHT_INDEX.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].disable").value(hasItem(DEFAULT_DISABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].disableReason").value(hasItem(DEFAULT_DISABLE_REASON)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restTireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTireShouldNotBeFound(String filter) throws Exception {
        restTireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTire() throws Exception {
        // Get the tire
        restTireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void putExistingTire() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tire
        Tire updatedTire = tireRepository.findById(insertedTire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTire are not directly saved in db
        em.detach(updatedTire);
        tireRepository.flush();
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
            .description(UPDATED_DESCRIPTION)
            .version(DEFAULT_VERSION);
        TireDTO tireDTO = tireMapper.toDto(updatedTire);

        restTireMockMvc
            .perform(put(ENTITY_API_URL_ID, tireDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tireDTO)))
            .andExpect(status().isOk());

        // Validate the Tire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTireToMatchAllProperties(updatedTire);
        //tireRepository.deleteAll();
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void partialUpdateTireWithPatch() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tire using partial update
        Tire partialUpdatedTire = new Tire();
        partialUpdatedTire.setId(insertedTire.getId());

        partialUpdatedTire
            .price(UPDATED_PRICE)
            .tireType(UPDATED_TIRE_TYPE)
            .speedIndex(UPDATED_SPEED_INDEX)
            .weightIndex(UPDATED_WEIGHT_INDEX)
            .disable(UPDATED_DISABLE)
            .disableReason(UPDATED_DISABLE_REASON)
            .description(UPDATED_DESCRIPTION)
            .version(DEFAULT_VERSION);

        restTireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTire))
            )
            .andExpect(status().isOk());

        // Validate the Tire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTireUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTire, insertedTire), getPersistedTire(insertedTire));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void fullUpdateTireWithPatch() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tire using partial update
        Tire partialUpdatedTire = new Tire();
        partialUpdatedTire.setId(insertedTire.getId());

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
            .description(UPDATED_DESCRIPTION)
            .version(DEFAULT_VERSION);

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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
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
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void deleteTire() throws Exception {
        // Initialize the database
        insertedTire = tireRepository.saveAndFlush(tire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tire
        restTireMockMvc
            .perform(delete(ENTITY_API_URL_ID, insertedTire.getId()).accept(MediaType.APPLICATION_JSON))
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

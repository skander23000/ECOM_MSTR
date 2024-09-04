package fr.ecom.mstr.tire.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ecom.mstr.tire.IntegrationTest;
import fr.ecom.mstr.tire.domain.ItemListLock;
import fr.ecom.mstr.tire.domain.Tire;
import fr.ecom.mstr.tire.repository.ItemListLockRepository;
import fr.ecom.mstr.tire.repository.TireRepository;
import fr.ecom.mstr.tire.service.ItemListLockService;
import fr.ecom.mstr.tire.service.dto.ItemListLockDTO;
import fr.ecom.mstr.tire.service.mapper.ItemListLockMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static fr.ecom.mstr.tire.domain.ItemListLockAsserts.*;
import static fr.ecom.mstr.tire.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ItemListLockResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemListLockResourceIT {

    private static final UUID DEFAULT_USER_UUID = UUID.randomUUID();
    private static final UUID UPDATED_USER_UUID = UUID.randomUUID();

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Instant DEFAULT_LOCK_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LOCK_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/item-list-locks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TireRepository tireRepository;

    @Autowired
    private ItemListLockRepository itemListLockRepository;

    @Autowired
    private ItemListLockService itemListLockService;

    @Autowired
    private ItemListLockMapper itemListLockMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemListLockMockMvc;

    private ItemListLock itemListLock;

    private ItemListLock insertedItemListLock;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemListLock createEntity() {
        return new ItemListLock().userUuid(DEFAULT_USER_UUID).quantity(DEFAULT_QUANTITY).lockTime(DEFAULT_LOCK_TIME);
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemListLock createUpdatedEntity() {
        return new ItemListLock().userUuid(UPDATED_USER_UUID).quantity(UPDATED_QUANTITY).lockTime(UPDATED_LOCK_TIME);
    }

    @BeforeEach
    public void initTest() {
        itemListLock = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedItemListLock != null) {
            itemListLockRepository.delete(insertedItemListLock);
            insertedItemListLock = null;
        }
    }

    @Test
    @Transactional
    void createItemListLock() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ItemListLock
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);
        var returnedItemListLockDTO = om.readValue(
            restItemListLockMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemListLockDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ItemListLockDTO.class
        );

        // Validate the ItemListLock in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedItemListLock = itemListLockMapper.toEntity(returnedItemListLockDTO);
        assertItemListLockUpdatableFieldsEquals(returnedItemListLock, getPersistedItemListLock(returnedItemListLock));

        insertedItemListLock = returnedItemListLock;
    }

    @Test
    @Transactional
    void createItemListLockWithExistingId() throws Exception {
        // Create the ItemListLock with an existing ID
        itemListLock.setId(1L);
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemListLockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemListLockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemListLock in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserUuidIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        itemListLock.setUserUuid(null);

        // Create the ItemListLock, which fails.
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);

        restItemListLockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemListLockDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        itemListLock.setQuantity(null);

        // Create the ItemListLock, which fails.
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);

        restItemListLockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemListLockDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLockTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        itemListLock.setLockTime(null);

        // Create the ItemListLock, which fails.
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);

        restItemListLockMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemListLockDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemListLocks() throws Exception {
        // Initialize the database
        insertedItemListLock = itemListLockRepository.saveAndFlush(itemListLock);

        // Get all the itemListLockList
        restItemListLockMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemListLock.getId().intValue())))
            .andExpect(jsonPath("$.[*].userUuid").value(hasItem(DEFAULT_USER_UUID.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].lockTime").value(hasItem(DEFAULT_LOCK_TIME.toString())));
    }

    @Test
    @Transactional
    void getItemListLock() throws Exception {
        // Initialize the database
        insertedItemListLock = itemListLockRepository.saveAndFlush(itemListLock);

        // Get the itemListLock
        restItemListLockMockMvc
            .perform(get(ENTITY_API_URL_ID, itemListLock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemListLock.getId().intValue()))
            .andExpect(jsonPath("$.userUuid").value(DEFAULT_USER_UUID.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.lockTime").value(DEFAULT_LOCK_TIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingItemListLock() throws Exception {
        // Get the itemListLock
        restItemListLockMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItemListLock() throws Exception {
        // Initialize the database
        insertedItemListLock = itemListLockRepository.saveAndFlush(itemListLock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemListLock
        ItemListLock updatedItemListLock = itemListLockRepository.findById(itemListLock.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedItemListLock are not directly saved in db
        em.detach(updatedItemListLock);
        updatedItemListLock.userUuid(UPDATED_USER_UUID).quantity(UPDATED_QUANTITY).lockTime(UPDATED_LOCK_TIME);
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(updatedItemListLock);

        restItemListLockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemListLockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemListLockDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemListLock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedItemListLockToMatchAllProperties(updatedItemListLock);
    }

    @Test
    @Transactional
    void putNonExistingItemListLock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemListLock.setId(longCount.incrementAndGet());

        // Create the ItemListLock
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemListLockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemListLockDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemListLockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemListLock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemListLock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemListLock.setId(longCount.incrementAndGet());

        // Create the ItemListLock
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemListLockMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(itemListLockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemListLock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemListLock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemListLock.setId(longCount.incrementAndGet());

        // Create the ItemListLock
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemListLockMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(itemListLockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemListLock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemListLockWithPatch() throws Exception {
        // Initialize the database
        insertedItemListLock = itemListLockRepository.saveAndFlush(itemListLock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemListLock using partial update
        ItemListLock partialUpdatedItemListLock = new ItemListLock();
        partialUpdatedItemListLock.setId(itemListLock.getId());

        restItemListLockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemListLock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItemListLock))
            )
            .andExpect(status().isOk());

        // Validate the ItemListLock in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemListLockUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedItemListLock, itemListLock),
            getPersistedItemListLock(itemListLock)
        );
    }

    @Test
    @Transactional
    void fullUpdateItemListLockWithPatch() throws Exception {
        // Initialize the database
        insertedItemListLock = itemListLockRepository.saveAndFlush(itemListLock);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the itemListLock using partial update
        ItemListLock partialUpdatedItemListLock = new ItemListLock();
        partialUpdatedItemListLock.setId(itemListLock.getId());

        partialUpdatedItemListLock.userUuid(UPDATED_USER_UUID).quantity(UPDATED_QUANTITY).lockTime(UPDATED_LOCK_TIME);

        restItemListLockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemListLock.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedItemListLock))
            )
            .andExpect(status().isOk());

        // Validate the ItemListLock in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertItemListLockUpdatableFieldsEquals(partialUpdatedItemListLock, getPersistedItemListLock(partialUpdatedItemListLock));
    }

    @Test
    @Transactional
    void patchNonExistingItemListLock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemListLock.setId(longCount.incrementAndGet());

        // Create the ItemListLock
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemListLockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemListLockDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemListLockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemListLock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemListLock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemListLock.setId(longCount.incrementAndGet());

        // Create the ItemListLock
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemListLockMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(itemListLockDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemListLock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemListLock() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        itemListLock.setId(longCount.incrementAndGet());

        // Create the ItemListLock
        ItemListLockDTO itemListLockDTO = itemListLockMapper.toDto(itemListLock);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemListLockMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(itemListLockDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemListLock in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemListLock() throws Exception {
        // Initialize the database
        insertedItemListLock = itemListLockRepository.saveAndFlush(itemListLock);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the itemListLock
        restItemListLockMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemListLock.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    void cleanupLockAndRestock() throws Exception {
        // Initialize the database
        Tire tire = TireResourceIT.createEntity();
        tire.setQuantity(2);
        tire = tireRepository.saveAndFlush(tire);

        itemListLock.setTire(tire);
        itemListLock.setQuantity(10);
        itemListLock.setLockTime(Instant.now().minus(5, ChronoUnit.HOURS));
        itemListLockRepository.saveAndFlush(itemListLock);

        ItemListLock item = createEntity();
        item.setQuantity(200);
        item.setTire(tire);
        item.setLockTime(Instant.now().plus(5, ChronoUnit.HOURS));
        itemListLockRepository.saveAndFlush(item);

        int databaseCount = itemListLockRepository.findAll().size();
        this.itemListLockService.cleanupLockAndRestock();

        Optional<Tire> finalTire = this.tireRepository.findById(tire.getId());
        int databaseCountFinal = itemListLockRepository.findAll().size();

        assertThat(databaseCount).isGreaterThan(databaseCountFinal);
        assertThat(databaseCountFinal).isOne();
        assertThat(finalTire).isPresent();
        assertThat(finalTire.orElseThrow().getQuantity()).isEqualTo(12);
    }

    @Test
    @Transactional
    void checkTireAvailability() throws Exception {
        // Initialize the database
        Tire tire = TireResourceIT.createEntity();
        tire.setQuantity(2);
        tire = tireRepository.saveAndFlush(tire);
        itemListLock.setTire(tire);
        insertedItemListLock = itemListLockRepository.saveAndFlush(itemListLock);
        int databaseCount = itemListLockRepository.findAll().size();

        // Check availability for 1 item of an existing lock
        restItemListLockMockMvc
            .perform(get(ENTITY_API_URL + "/check-availability?userUuid=" +
                itemListLock.getUserUuid().toString() + "&tireId=" + tire.getId() + "&quantity=1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").value(true));

        // Check availability for 30 item of an existing lock but without enough stock
        restItemListLockMockMvc
            .perform(get(ENTITY_API_URL + "/check-availability?userUuid=" +
                itemListLock.getUserUuid().toString() + "&tireId=" + tire.getId() + "&quantity=30"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").value(false));

        // Check availability for 30 item of a non-existing lock
        restItemListLockMockMvc
            .perform(get(ENTITY_API_URL + "/check-availability?userUuid=" +
                UUID.randomUUID().toString() + "&tireId=" + tire.getId() + "&quantity=30"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").value(false));

        // Check availability for 1 item of a non-existing lock
        restItemListLockMockMvc
            .perform(get(ENTITY_API_URL + "/check-availability?userUuid=" +
                UUID.randomUUID().toString() + "&tireId=" + tire.getId() + "&quantity=1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").value(true));
        int databaseCountAfter = itemListLockRepository.findAll().size();
        // 2 lock should be present
        assertThat(databaseCount + 1).isEqualTo(databaseCountAfter);

        itemListLockRepository.deleteAll();
        tireRepository.deleteAll();
    }

    protected long getRepositoryCount() {
        return itemListLockRepository.count();
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

    protected ItemListLock getPersistedItemListLock(ItemListLock itemListLock) {
        return itemListLockRepository.findById(itemListLock.getId()).orElseThrow();
    }

    protected void assertPersistedItemListLockToMatchAllProperties(ItemListLock expectedItemListLock) {
        assertItemListLockAllPropertiesEquals(expectedItemListLock, getPersistedItemListLock(expectedItemListLock));
    }

    protected void assertPersistedItemListLockToMatchUpdatableProperties(ItemListLock expectedItemListLock) {
        assertItemListLockAllUpdatablePropertiesEquals(expectedItemListLock, getPersistedItemListLock(expectedItemListLock));
    }
}

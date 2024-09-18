package fr.ecom.mstr.tire.service;

import fr.ecom.mstr.tire.domain.OrderItem;
import fr.ecom.mstr.tire.repository.OrderItemRepository;
import fr.ecom.mstr.tire.service.dto.CustomerDTO;
import fr.ecom.mstr.tire.service.dto.CustomerOrderDTO;
import fr.ecom.mstr.tire.service.dto.ItemListLockDTO;
import fr.ecom.mstr.tire.service.dto.OrderItemDTO;
import fr.ecom.mstr.tire.service.mapper.OrderItemMapper;
import fr.ecom.mstr.tire.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link fr.ecom.mstr.tire.domain.OrderItem}.
 */
@Service
@Transactional
public class OrderItemService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderItemService.class);

    private final ItemListLockService itemListLockService;

    private final CustomerOrderService customerOrderService;

    private final OrderItemRepository orderItemRepository;

    private final OrderItemMapper orderItemMapper;
    private final MailService mailService;

    public OrderItemService(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper, ItemListLockService itemListLockService, CustomerOrderService customerOrderService, MailService mailService) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
        this.itemListLockService = itemListLockService;
        this.customerOrderService = customerOrderService;
        this.mailService = mailService;
    }

    /**
     * Save all orderItem.
     *
     * @param orderItemsDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public List<OrderItemDTO> saveAll(List<OrderItemDTO> orderItemsDTO, UUID userUuid) {
        LOG.debug("Request to save OrderItem : {}", orderItemsDTO);
        if (orderItemsDTO.isEmpty()) {
            throw new InternalError("Shopping cart can't be empty!");
        }

        // Check Lock = Shopping cart
        List<ItemListLockDTO> reservedItems = this.itemListLockService.findAllItemListLockByUserUuid(userUuid);
        Map<Long, ItemListLockDTO> reservationPerTireId = new HashMap<>();
        List<Long> reservationIds = new ArrayList<>();
        reservedItems.forEach(lock -> {
            reservationPerTireId.put(lock.getTire().getId(), lock);
            reservationIds.add(lock.getId());
        });

        Map<Long, OrderItemDTO> orderPerTireId = new HashMap<>();
        orderItemsDTO.forEach(orderItem -> orderPerTireId.put(orderItem.getTire().getId(), orderItem));

        if (!orderPerTireId.keySet().equals(reservationPerTireId.keySet())) {
            throw new InternalError("The shopping cart has different items from the reservation");
        }
        if (orderPerTireId.keySet().stream().noneMatch(id -> checkQuantity(orderPerTireId, reservationPerTireId, id))) {
            throw new InternalError("Quantity not identical between the shopping cart and reservation");
        }

        CustomerOrderDTO customerOrder = getCustomerOrderFromOrderItemList(orderItemsDTO);
        customerOrder = this.customerOrderService.save(customerOrder);
        for (OrderItemDTO orderItem : orderItemsDTO) {
            orderItem.setCustomerOrder(customerOrder);
        }
        List<OrderItem> orderItems = orderItemsDTO.stream().map(this.orderItemMapper::toEntity)
            .collect(Collectors.toList());

        List<OrderItemDTO> Items = this.orderItemRepository.saveAll(orderItems)
            .stream().map(this.orderItemMapper::toDto).collect(Collectors.toList());

        // Delete lock
        this.itemListLockService.deleteAll(reservationIds);

        // Send mail
        // this.mailService.sendInvoicingEmail(Items, customerOrder);


        return Items;
    }

    private CustomerOrderDTO getCustomerOrderFromOrderItemList(List<OrderItemDTO> orderItemsDTO) {
        CustomerOrderDTO custOrder = null;
        CustomerDTO cust = null;
        for (OrderItemDTO orderItem : orderItemsDTO) {
            if (cust == null) {
                custOrder = orderItem.getCustomerOrder();
                if (custOrder == null || custOrder.getCustomer() == null) {
                    throw new InternalError("Customer information are missing");
                }
                cust = custOrder.getCustomer();
            } else if (!checkCustomerOrder(custOrder, orderItem.getCustomerOrder()) || !checkCustomer(cust, orderItem.getCustomerOrder().getCustomer())) {
                throw new InternalError("Customer information are not the same between order items");
            }
        }
        return custOrder;
    }

    private boolean checkCustomer(CustomerDTO customer1, CustomerDTO customer2) {
        return Objects.equals(customer1.getFirstName(), customer2.getFirstName()) &&
            Objects.equals(customer1.getLastName(), customer2.getLastName()) &&
            Objects.equals(customer1.getEmail(), customer2.getEmail()) &&
            Objects.equals(customer1.getAddress(), customer2.getAddress()) &&
            Objects.equals(customer1.getCity(), customer2.getCity()) &&
            Objects.equals(customer1.getZipCode(), customer2.getZipCode()) &&
            Objects.equals(customer1.getCountry(), customer2.getCountry()) &&
            Objects.equals(customer1.getPhoneNumber(), customer2.getPhoneNumber());
    }

    private boolean checkCustomerOrder(CustomerOrderDTO customerOrder1, CustomerOrderDTO customerOrder2) {
        return Objects.equals(customerOrder1.getOrderDate(), customerOrder2.getOrderDate()) &&
            Objects.equals(customerOrder1.getStatus(), customerOrder2.getStatus()) &&
            Objects.equals(customerOrder1.getTotalAmount(), customerOrder2.getTotalAmount()) &&
            Objects.equals(customerOrder1.getPaymentDate(), customerOrder2.getPaymentDate()) &&
            Objects.equals(customerOrder1.getPaymentMethod(), customerOrder2.getPaymentMethod()) &&
            Objects.equals(customerOrder1.getPaymentStatus(), customerOrder2.getPaymentStatus());
    }

    private boolean checkQuantity(Map<Long, OrderItemDTO> orderPerTireId,
                                  Map<Long, ItemListLockDTO> reservationPerTireId,
                                  Long tireId) {
        return Objects.equals(orderPerTireId.get(tireId).getQuantity(),
            reservationPerTireId.get(tireId).getQuantity());
    }

    /**
     * Save a orderItem.
     *
     * @param orderItemDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderItemDTO save(OrderItemDTO orderItemDTO) {
        LOG.debug("Request to save OrderItem : {}", orderItemDTO);
        OrderItem orderItem = this.orderItemMapper.toEntity(orderItemDTO);
        orderItem = this.orderItemRepository.save(orderItem);
        return this.orderItemMapper.toDto(orderItem);
    }

    /**
     * Update a orderItem.
     *
     * @param orderItemDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderItemDTO update(OrderItemDTO orderItemDTO) {
        LOG.debug("Request to update OrderItem : {}", orderItemDTO);
        OrderItem orderItem = this.orderItemMapper.toEntity(orderItemDTO);
        orderItem = this.orderItemRepository.save(orderItem);
        return this.orderItemMapper.toDto(orderItem);
    }

    /**
     * Partially update a orderItem.
     *
     * @param orderItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrderItemDTO> partialUpdate(OrderItemDTO orderItemDTO) {
        LOG.debug("Request to partially update OrderItem : {}", orderItemDTO);

        return this.orderItemRepository
            .findById(orderItemDTO.getId())
            .map(existingOrderItem -> {
                this.orderItemMapper.partialUpdate(existingOrderItem, orderItemDTO);

                return existingOrderItem;
            })
            .map(this.orderItemRepository::save)
            .map(this.orderItemMapper::toDto);
    }

    /**
     * Get all the orderItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrderItemDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OrderItems");
        return this.orderItemRepository.findAll(pageable).map(orderItemMapper::toDto);
    }

    /**
     * Get one orderItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderItemDTO> findOne(Long id) {
        LOG.debug("Request to get OrderItem : {}", id);
        return this.orderItemRepository.findById(id).map(orderItemMapper::toDto);
    }

    /**
     * Delete the orderItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrderItem : {}", id);
        this.orderItemRepository.deleteById(id);
    }
}

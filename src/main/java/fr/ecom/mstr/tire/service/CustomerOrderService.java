package fr.ecom.mstr.tire.service;

import fr.ecom.mstr.tire.domain.CustomerOrder;
import fr.ecom.mstr.tire.repository.CustomerOrderRepository;
import fr.ecom.mstr.tire.service.dto.CustomerOrderDTO;
import fr.ecom.mstr.tire.service.mapper.CustomerOrderMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.ecom.mstr.tire.domain.CustomerOrder}.
 */
@Service
@Transactional
public class CustomerOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerOrderService.class);

    private final CustomerOrderRepository customerOrderRepository;

    private final CustomerOrderMapper customerOrderMapper;

    public CustomerOrderService(CustomerOrderRepository customerOrderRepository, CustomerOrderMapper customerOrderMapper) {
        this.customerOrderRepository = customerOrderRepository;
        this.customerOrderMapper = customerOrderMapper;
    }

    /**
     * Save a customerOrder.
     *
     * @param customerOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerOrderDTO save(CustomerOrderDTO customerOrderDTO) {
        LOG.debug("Request to save CustomerOrder : {}", customerOrderDTO);
        CustomerOrder customerOrder = customerOrderMapper.toEntity(customerOrderDTO);
        customerOrder = customerOrderRepository.save(customerOrder);
        return customerOrderMapper.toDto(customerOrder);
    }

    /**
     * Update a customerOrder.
     *
     * @param customerOrderDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerOrderDTO update(CustomerOrderDTO customerOrderDTO) {
        LOG.debug("Request to update CustomerOrder : {}", customerOrderDTO);
        CustomerOrder customerOrder = customerOrderMapper.toEntity(customerOrderDTO);
        customerOrder = customerOrderRepository.save(customerOrder);
        return customerOrderMapper.toDto(customerOrder);
    }

    /**
     * Partially update a customerOrder.
     *
     * @param customerOrderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomerOrderDTO> partialUpdate(CustomerOrderDTO customerOrderDTO) {
        LOG.debug("Request to partially update CustomerOrder : {}", customerOrderDTO);

        return customerOrderRepository
            .findById(customerOrderDTO.getId())
            .map(existingCustomerOrder -> {
                customerOrderMapper.partialUpdate(existingCustomerOrder, customerOrderDTO);

                return existingCustomerOrder;
            })
            .map(customerOrderRepository::save)
            .map(customerOrderMapper::toDto);
    }

    /**
     * Get all the customerOrders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerOrderDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CustomerOrders");
        return customerOrderRepository.findAll(pageable).map(customerOrderMapper::toDto);
    }

    /**
     *  Get all the customerOrders where Customer is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerOrderDTO> findAllWhereCustomerIsNull() {
        LOG.debug("Request to get all customerOrders where Customer is null");
        return StreamSupport.stream(customerOrderRepository.findAll().spliterator(), false)
            .filter(customerOrder -> customerOrder.getCustomer() == null)
            .map(customerOrderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one customerOrder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerOrderDTO> findOne(Long id) {
        LOG.debug("Request to get CustomerOrder : {}", id);
        return customerOrderRepository.findById(id).map(customerOrderMapper::toDto);
    }

    /**
     * Delete the customerOrder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CustomerOrder : {}", id);
        customerOrderRepository.deleteById(id);
    }
}

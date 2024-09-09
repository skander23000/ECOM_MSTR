package fr.ecom.mstr.tire.service.mapper;

import fr.ecom.mstr.tire.domain.Customer;
import fr.ecom.mstr.tire.domain.CustomerOrder;
import fr.ecom.mstr.tire.service.dto.CustomerDTO;
import fr.ecom.mstr.tire.service.dto.CustomerOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "customerOrder", source = "customerOrder", qualifiedByName = "customerOrderId")
    CustomerDTO toDto(Customer s);

    @Named("customerOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerOrderDTO toDtoCustomerOrderId(CustomerOrder customerOrder);
}

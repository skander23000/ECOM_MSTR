package fr.ecom.mstr.tire.service.mapper;

import fr.ecom.mstr.tire.domain.Customer;
import fr.ecom.mstr.tire.service.dto.CustomerDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
}

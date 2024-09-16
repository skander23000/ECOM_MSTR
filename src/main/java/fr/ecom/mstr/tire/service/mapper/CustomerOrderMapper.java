package fr.ecom.mstr.tire.service.mapper;

import fr.ecom.mstr.tire.domain.CustomerOrder;
import fr.ecom.mstr.tire.service.dto.CustomerOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerOrder} and its DTO {@link CustomerOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface CustomerOrderMapper extends EntityMapper<CustomerOrderDTO, CustomerOrder> {}

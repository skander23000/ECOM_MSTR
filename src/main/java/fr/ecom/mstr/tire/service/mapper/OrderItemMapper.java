package fr.ecom.mstr.tire.service.mapper;

import fr.ecom.mstr.tire.domain.CustomerOrder;
import fr.ecom.mstr.tire.domain.OrderItem;
import fr.ecom.mstr.tire.domain.Tire;
import fr.ecom.mstr.tire.service.dto.CustomerOrderDTO;
import fr.ecom.mstr.tire.service.dto.OrderItemDTO;
import fr.ecom.mstr.tire.service.dto.TireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, CustomerOrderMapper.class, TireMapper.class})
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
}

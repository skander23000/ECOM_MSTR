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
@Mapper(componentModel = "spring")
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {
    @Mapping(target = "customerOrder", source = "customerOrder", qualifiedByName = "customerOrderId")
    @Mapping(target = "tire", source = "tire", qualifiedByName = "tireId")
    OrderItemDTO toDto(OrderItem s);

    @Named("customerOrderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerOrderDTO toDtoCustomerOrderId(CustomerOrder customerOrder);

    @Named("tireId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TireDTO toDtoTireId(Tire tire);
}

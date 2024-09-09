package fr.ecom.mstr.tire.service.mapper;

import fr.ecom.mstr.tire.domain.ItemListLock;
import fr.ecom.mstr.tire.domain.Tire;
import fr.ecom.mstr.tire.service.dto.ItemListLockDTO;
import fr.ecom.mstr.tire.service.dto.TireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemListLock} and its DTO {@link ItemListLockDTO}.
 */
@Mapper(componentModel = "spring")
public interface ItemListLockMapper extends EntityMapper<ItemListLockDTO, ItemListLock> {
    @Mapping(target = "tire", source = "tire", qualifiedByName = "tireId")
    ItemListLockDTO toDto(ItemListLock s);

    @Named("tireId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TireDTO toDtoTireId(Tire tire);
}

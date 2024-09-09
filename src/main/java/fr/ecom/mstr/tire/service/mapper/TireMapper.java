package fr.ecom.mstr.tire.service.mapper;

import fr.ecom.mstr.tire.domain.Tire;
import fr.ecom.mstr.tire.domain.TireBrand;
import fr.ecom.mstr.tire.service.dto.TireBrandDTO;
import fr.ecom.mstr.tire.service.dto.TireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tire} and its DTO {@link TireDTO}.
 */
@Mapper(componentModel = "spring")
public interface TireMapper extends EntityMapper<TireDTO, Tire> {
    @Mapping(target = "tireBrand", source = "tireBrand", qualifiedByName = "tireBrandName")
    TireDTO toDto(Tire s);

    @Named("tireBrandName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    TireBrandDTO toDtoTireBrandName(TireBrand tireBrand);
}

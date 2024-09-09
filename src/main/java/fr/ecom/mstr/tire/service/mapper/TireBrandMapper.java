package fr.ecom.mstr.tire.service.mapper;

import fr.ecom.mstr.tire.domain.TireBrand;
import fr.ecom.mstr.tire.service.dto.TireBrandDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TireBrand} and its DTO {@link TireBrandDTO}.
 */
@Mapper(componentModel = "spring")
public interface TireBrandMapper extends EntityMapper<TireBrandDTO, TireBrand> {}

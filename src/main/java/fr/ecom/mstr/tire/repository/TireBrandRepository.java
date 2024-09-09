package fr.ecom.mstr.tire.repository;

import fr.ecom.mstr.tire.domain.TireBrand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TireBrand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TireBrandRepository extends JpaRepository<TireBrand, Long> {}

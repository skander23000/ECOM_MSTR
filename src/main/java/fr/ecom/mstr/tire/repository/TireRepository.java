package fr.ecom.mstr.tire.repository;

import fr.ecom.mstr.tire.domain.Tire;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tire entity.
 */
@Repository
public interface TireRepository extends JpaRepository<Tire, Long> {
    default Optional<Tire> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Tire> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Tire> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select tire from Tire tire left join fetch tire.tireBrand", countQuery = "select count(tire) from Tire tire")
    Page<Tire> findAllWithToOneRelationships(Pageable pageable);

    @Query("select tire from Tire tire left join fetch tire.tireBrand")
    List<Tire> findAllWithToOneRelationships();

    @Query("select tire from Tire tire left join fetch tire.tireBrand where tire.id =:id")
    Optional<Tire> findOneWithToOneRelationships(@Param("id") Long id);
}

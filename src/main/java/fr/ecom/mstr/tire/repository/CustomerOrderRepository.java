package fr.ecom.mstr.tire.repository;

import fr.ecom.mstr.tire.domain.CustomerOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {}

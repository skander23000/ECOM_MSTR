package fr.ecom.mstr.tire.repository;

import fr.ecom.mstr.tire.domain.ItemListLock;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ItemListLock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemListLockRepository extends JpaRepository<ItemListLock, Long> {}

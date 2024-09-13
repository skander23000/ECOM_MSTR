package fr.ecom.mstr.tire.repository;

import fr.ecom.mstr.tire.domain.ItemListLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for the ItemListLock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemListLockRepository extends JpaRepository<ItemListLock, Long> {
    @Query(
        value = "Select case when (ill.tire.quantity - (:quantity - ill.quantity)) >= 0 then true else false end " +
            "from ItemListLock ill where ill.userUuid = :userUuid and ill.tire.id = :tireId"
    )
    Boolean isNewQuantityValid(@Param("userUuid") UUID userUuid, @Param("tireId") Long tireId, @Param("quantity") Integer quantity);

    @Query("Select ill from ItemListLock ill left join fetch ill.tire where ill.userUuid = :userUuid and ill.tire.id = :tireId")
    Optional<ItemListLock> findByUuidAndTireId(@Param("userUuid") UUID userUuid, @Param("tireId") Long tireId);

    @Query("Select ill from ItemListLock ill left join fetch ill.tire where ill.userUuid = :userUuid")
    List<ItemListLock> findByUuid(@Param("userUuid") UUID userUuid);

    @Query("Select exists (Select 1 from ItemListLock ill where ill.userUuid = :userUuid)")
    Boolean isItemListLockByUuid(@Param("userUuid") UUID userUuid);

    @Modifying
    @Query("Update ItemListLock ill set ill.quantity = :quantity where ill.id = :id")
    void updateItemQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Modifying
    @Query("Update ItemListLock ill set ill.lockTime = :newTimer where ill.userUuid = :userUuid")
    void updateItemTimer(@Param("newTimer") Instant newTimer, @Param("userUuid") UUID userUuid);

    @Query("Select ill from ItemListLock ill left join fetch ill.tire where CAST(ill.lockTime as timestamp) < CAST(:now as timestamp)")
    List<ItemListLock> findAllExpiredLock(@Param("now") Instant now);
}

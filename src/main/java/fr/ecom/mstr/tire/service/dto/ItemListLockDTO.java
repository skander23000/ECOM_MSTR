package fr.ecom.mstr.tire.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link fr.ecom.mstr.tire.domain.ItemListLock} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemListLockDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID userUuid;

    @NotNull
    private Integer quantity;

    @NotNull
    private Instant lockTime;

    private TireDTO tire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getLockTime() {
        return lockTime;
    }

    public void setLockTime(Instant lockTime) {
        this.lockTime = lockTime;
    }

    public TireDTO getTire() {
        return tire;
    }

    public void setTire(TireDTO tire) {
        this.tire = tire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemListLockDTO)) {
            return false;
        }

        ItemListLockDTO itemListLockDTO = (ItemListLockDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemListLockDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemListLockDTO{" +
            "id=" + getId() +
            ", userUuid='" + getUserUuid() + "'" +
            ", quantity=" + getQuantity() +
            ", lockTime='" + getLockTime() + "'" +
            ", tire=" + getTire() +
            "}";
    }
}

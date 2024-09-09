package fr.ecom.mstr.tire.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * A ItemListLock.
 */
@Entity
@Table(name = "item_list_lock")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemListLock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "lock_time", nullable = false)
    private Instant lockTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "itemListLocks", "tireBrand" }, allowSetters = true)
    private Tire tire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ItemListLock id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserUuid() {
        return this.userUuid;
    }

    public ItemListLock userUuid(UUID userUuid) {
        this.setUserUuid(userUuid);
        return this;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public ItemListLock quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instant getLockTime() {
        return this.lockTime;
    }

    public ItemListLock lockTime(Instant lockTime) {
        this.setLockTime(lockTime);
        return this;
    }

    public void setLockTime(Instant lockTime) {
        this.lockTime = lockTime;
    }

    public Tire getTire() {
        return this.tire;
    }

    public void setTire(Tire tire) {
        this.tire = tire;
    }

    public ItemListLock tire(Tire tire) {
        this.setTire(tire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemListLock)) {
            return false;
        }
        return getId() != null && getId().equals(((ItemListLock) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemListLock{" +
            "id=" + getId() +
            ", userUuid='" + getUserUuid() + "'" +
            ", quantity=" + getQuantity() +
            ", lockTime='" + getLockTime() + "'" +
            "}";
    }
}

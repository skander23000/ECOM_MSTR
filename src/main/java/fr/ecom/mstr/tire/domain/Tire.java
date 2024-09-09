package fr.ecom.mstr.tire.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.ecom.mstr.tire.domain.enumeration.ChargeIndex;
import fr.ecom.mstr.tire.domain.enumeration.SpeedIndex;
import fr.ecom.mstr.tire.domain.enumeration.TireType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Tire.
 */
@Entity
@Table(name = "tire")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "reference", unique = true)
    private String reference;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "tire_width", nullable = false)
    private String tireWidth;

    @NotNull
    @Column(name = "tire_height", nullable = false)
    private String tireHeight;

    @NotNull
    @Column(name = "tire_diameter", nullable = false)
    private String tireDiameter;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tire_type", nullable = false)
    private TireType tireType;

    @NotNull
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "speed_index", nullable = false)
    private SpeedIndex speedIndex;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "weight_index", nullable = false)
    private ChargeIndex weightIndex;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "disable", nullable = false)
    private Boolean disable;

    @Column(name = "disable_reason")
    private String disableReason;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tire")
    @JsonIgnoreProperties(value = { "tire" }, allowSetters = true)
    private Set<ItemListLock> itemListLocks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tires" }, allowSetters = true)
    private TireBrand tireBrand;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return this.reference;
    }

    public Tire reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return this.name;
    }

    public Tire name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Tire price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTireWidth() {
        return this.tireWidth;
    }

    public Tire tireWidth(String tireWidth) {
        this.setTireWidth(tireWidth);
        return this;
    }

    public void setTireWidth(String tireWidth) {
        this.tireWidth = tireWidth;
    }

    public String getTireHeight() {
        return this.tireHeight;
    }

    public Tire tireHeight(String tireHeight) {
        this.setTireHeight(tireHeight);
        return this;
    }

    public void setTireHeight(String tireHeight) {
        this.tireHeight = tireHeight;
    }

    public String getTireDiameter() {
        return this.tireDiameter;
    }

    public Tire tireDiameter(String tireDiameter) {
        this.setTireDiameter(tireDiameter);
        return this;
    }

    public void setTireDiameter(String tireDiameter) {
        this.tireDiameter = tireDiameter;
    }

    public TireType getTireType() {
        return this.tireType;
    }

    public Tire tireType(TireType tireType) {
        this.setTireType(tireType);
        return this;
    }

    public void setTireType(TireType tireType) {
        this.tireType = tireType;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Tire imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SpeedIndex getSpeedIndex() {
        return this.speedIndex;
    }

    public Tire speedIndex(SpeedIndex speedIndex) {
        this.setSpeedIndex(speedIndex);
        return this;
    }

    public void setSpeedIndex(SpeedIndex speedIndex) {
        this.speedIndex = speedIndex;
    }

    public ChargeIndex getWeightIndex() {
        return this.weightIndex;
    }

    public Tire weightIndex(ChargeIndex weightIndex) {
        this.setWeightIndex(weightIndex);
        return this;
    }

    public void setWeightIndex(ChargeIndex weightIndex) {
        this.weightIndex = weightIndex;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public Tire quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getDisable() {
        return this.disable;
    }

    public Tire disable(Boolean disable) {
        this.setDisable(disable);
        return this;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getDisableReason() {
        return this.disableReason;
    }

    public Tire disableReason(String disableReason) {
        this.setDisableReason(disableReason);
        return this;
    }

    public void setDisableReason(String disableReason) {
        this.disableReason = disableReason;
    }

    public String getDescription() {
        return this.description;
    }

    public Tire description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ItemListLock> getItemListLocks() {
        return this.itemListLocks;
    }

    public void setItemListLocks(Set<ItemListLock> itemListLocks) {
        if (this.itemListLocks != null) {
            this.itemListLocks.forEach(i -> i.setTire(null));
        }
        if (itemListLocks != null) {
            itemListLocks.forEach(i -> i.setTire(this));
        }
        this.itemListLocks = itemListLocks;
    }

    public Tire itemListLocks(Set<ItemListLock> itemListLocks) {
        this.setItemListLocks(itemListLocks);
        return this;
    }

    public Tire addItemListLocks(ItemListLock itemListLock) {
        this.itemListLocks.add(itemListLock);
        itemListLock.setTire(this);
        return this;
    }

    public Tire removeItemListLocks(ItemListLock itemListLock) {
        this.itemListLocks.remove(itemListLock);
        itemListLock.setTire(null);
        return this;
    }

    public TireBrand getTireBrand() {
        return this.tireBrand;
    }

    public void setTireBrand(TireBrand tireBrand) {
        this.tireBrand = tireBrand;
    }

    public Tire tireBrand(TireBrand tireBrand) {
        this.setTireBrand(tireBrand);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tire)) {
            return false;
        }
        return getId() != null && getId().equals(((Tire) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tire{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", name='" + getName() + "'" +
            ", price=" + getPrice() +
            ", tireWidth='" + getTireWidth() + "'" +
            ", tireHeight='" + getTireHeight() + "'" +
            ", tireDiameter='" + getTireDiameter() + "'" +
            ", tireType='" + getTireType() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", speedIndex='" + getSpeedIndex() + "'" +
            ", weightIndex='" + getWeightIndex() + "'" +
            ", quantity=" + getQuantity() +
            ", disable='" + getDisable() + "'" +
            ", disableReason='" + getDisableReason() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}

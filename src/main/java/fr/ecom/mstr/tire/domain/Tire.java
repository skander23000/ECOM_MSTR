package fr.ecom.mstr.tire.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.ecom.mstr.tire.domain.enumeration.ChargeIndex;
import fr.ecom.mstr.tire.domain.enumeration.SpeedIndex;
import fr.ecom.mstr.tire.domain.enumeration.TireType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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
    private BigDecimal tireWidth;

    @NotNull
    @Column(name = "tire_height", nullable = false)
    private BigDecimal tireHeight;

    @NotNull
    @Column(name = "tire_diameter", nullable = false)
    private BigDecimal tireDiameter;

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
    @JsonIgnoreProperties(value = {"tire"}, allowSetters = true)
    private Set<ItemListLock> itemListLocks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"tires"}, allowSetters = true)
    private TireBrand tireBrand;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tire id(Long id) {
        this.setId(id);
        return this;
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Tire reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tire name(String name) {
        this.setName(name);
        return this;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Tire price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public BigDecimal getTireWidth() {
        return this.tireWidth;
    }

    public void setTireWidth(BigDecimal tireWidth) {
        this.tireWidth = tireWidth;
    }

    public Tire tireWidth(BigDecimal tireWidth) {
        this.setTireWidth(tireWidth);
        return this;
    }

    public BigDecimal getTireHeight() {
        return this.tireHeight;
    }

    public void setTireHeight(BigDecimal tireHeight) {
        this.tireHeight = tireHeight;
    }

    public Tire tireHeight(BigDecimal tireHeight) {
        this.setTireHeight(tireHeight);
        return this;
    }

    public BigDecimal getTireDiameter() {
        return this.tireDiameter;
    }

    public void setTireDiameter(BigDecimal tireDiameter) {
        this.tireDiameter = tireDiameter;
    }

    public Tire tireDiameter(BigDecimal tireDiameter) {
        this.setTireDiameter(tireDiameter);
        return this;
    }

    public TireType getTireType() {
        return this.tireType;
    }

    public void setTireType(TireType tireType) {
        this.tireType = tireType;
    }

    public Tire tireType(TireType tireType) {
        this.setTireType(tireType);
        return this;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Tire imageUrl(String imageUrl) {
        this.setImageUrl(imageUrl);
        return this;
    }

    public SpeedIndex getSpeedIndex() {
        return this.speedIndex;
    }

    public void setSpeedIndex(SpeedIndex speedIndex) {
        this.speedIndex = speedIndex;
    }

    public Tire speedIndex(SpeedIndex speedIndex) {
        this.setSpeedIndex(speedIndex);
        return this;
    }

    public ChargeIndex getWeightIndex() {
        return this.weightIndex;
    }

    public void setWeightIndex(ChargeIndex weightIndex) {
        this.weightIndex = weightIndex;
    }

    public Tire weightIndex(ChargeIndex weightIndex) {
        this.setWeightIndex(weightIndex);
        return this;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Tire quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public Boolean getDisable() {
        return this.disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public Tire disable(Boolean disable) {
        this.setDisable(disable);
        return this;
    }

    public String getDisableReason() {
        return this.disableReason;
    }

    public void setDisableReason(String disableReason) {
        this.disableReason = disableReason;
    }

    public Tire disableReason(String disableReason) {
        this.setDisableReason(disableReason);
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Tire description(String description) {
        this.setDescription(description);
        return this;
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

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Tire version(Integer version) {
        this.setVersion(version);
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
            ", version='" + getVersion() + "'" +
            "}";
    }
}

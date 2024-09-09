package fr.ecom.mstr.tire.service.dto;

import fr.ecom.mstr.tire.domain.enumeration.ChargeIndex;
import fr.ecom.mstr.tire.domain.enumeration.SpeedIndex;
import fr.ecom.mstr.tire.domain.enumeration.TireType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link fr.ecom.mstr.tire.domain.Tire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TireDTO implements Serializable {

    private Long id;

    private String reference;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String tireWidth;

    @NotNull
    private String tireHeight;

    @NotNull
    private String tireDiameter;

    @NotNull
    private TireType tireType;

    @NotNull
    private String imageUrl;

    @NotNull
    private SpeedIndex speedIndex;

    @NotNull
    private ChargeIndex weightIndex;

    @NotNull
    private Integer quantity;

    @NotNull
    private Boolean disable;

    private String disableReason;

    private String description;

    private TireBrandDTO tireBrand;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTireWidth() {
        return tireWidth;
    }

    public void setTireWidth(String tireWidth) {
        this.tireWidth = tireWidth;
    }

    public String getTireHeight() {
        return tireHeight;
    }

    public void setTireHeight(String tireHeight) {
        this.tireHeight = tireHeight;
    }

    public String getTireDiameter() {
        return tireDiameter;
    }

    public void setTireDiameter(String tireDiameter) {
        this.tireDiameter = tireDiameter;
    }

    public TireType getTireType() {
        return tireType;
    }

    public void setTireType(TireType tireType) {
        this.tireType = tireType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SpeedIndex getSpeedIndex() {
        return speedIndex;
    }

    public void setSpeedIndex(SpeedIndex speedIndex) {
        this.speedIndex = speedIndex;
    }

    public ChargeIndex getWeightIndex() {
        return weightIndex;
    }

    public void setWeightIndex(ChargeIndex weightIndex) {
        this.weightIndex = weightIndex;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

    public String getDisableReason() {
        return disableReason;
    }

    public void setDisableReason(String disableReason) {
        this.disableReason = disableReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TireBrandDTO getTireBrand() {
        return tireBrand;
    }

    public void setTireBrand(TireBrandDTO tireBrand) {
        this.tireBrand = tireBrand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TireDTO)) {
            return false;
        }

        TireDTO tireDTO = (TireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TireDTO{" +
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
            ", tireBrand=" + getTireBrand() +
            "}";
    }
}

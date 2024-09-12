package fr.ecom.mstr.tire.service.criteria;

import fr.ecom.mstr.tire.domain.enumeration.ChargeIndex;
import fr.ecom.mstr.tire.domain.enumeration.SpeedIndex;
import fr.ecom.mstr.tire.domain.enumeration.TireType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.ecom.mstr.tire.domain.Tire} entity. This class is used
 * in {@link fr.ecom.mstr.tire.web.rest.TireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TireCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TireType
     */
    public static class TireTypeFilter extends Filter<TireType> {

        public TireTypeFilter() {}

        public TireTypeFilter(TireTypeFilter filter) {
            super(filter);
        }

        @Override
        public TireTypeFilter copy() {
            return new TireTypeFilter(this);
        }
    }

    /**
     * Class for filtering SpeedIndex
     */
    public static class SpeedIndexFilter extends Filter<SpeedIndex> {

        public SpeedIndexFilter() {}

        public SpeedIndexFilter(SpeedIndexFilter filter) {
            super(filter);
        }

        @Override
        public SpeedIndexFilter copy() {
            return new SpeedIndexFilter(this);
        }
    }

    /**
     * Class for filtering ChargeIndex
     */
    public static class ChargeIndexFilter extends Filter<ChargeIndex> {

        public ChargeIndexFilter() {}

        public ChargeIndexFilter(ChargeIndexFilter filter) {
            super(filter);
        }

        @Override
        public ChargeIndexFilter copy() {
            return new ChargeIndexFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reference;

    private StringFilter name;

    private BigDecimalFilter price;

    private StringFilter tireWidth;

    private StringFilter tireHeight;

    private StringFilter tireDiameter;

    private TireTypeFilter tireType;

    private StringFilter imageUrl;

    private SpeedIndexFilter speedIndex;

    private ChargeIndexFilter weightIndex;

    private IntegerFilter quantity;

    private BooleanFilter disable;

    private StringFilter disableReason;

    private StringFilter description;

    private LongFilter itemListLocksId;

    private LongFilter tireBrandId;

    private Boolean distinct;

    public TireCriteria() {}

    public TireCriteria(TireCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.reference = other.optionalReference().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.tireWidth = other.optionalTireWidth().map(StringFilter::copy).orElse(null);
        this.tireHeight = other.optionalTireHeight().map(StringFilter::copy).orElse(null);
        this.tireDiameter = other.optionalTireDiameter().map(StringFilter::copy).orElse(null);
        this.tireType = other.optionalTireType().map(TireTypeFilter::copy).orElse(null);
        this.imageUrl = other.optionalImageUrl().map(StringFilter::copy).orElse(null);
        this.speedIndex = other.optionalSpeedIndex().map(SpeedIndexFilter::copy).orElse(null);
        this.weightIndex = other.optionalWeightIndex().map(ChargeIndexFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.disable = other.optionalDisable().map(BooleanFilter::copy).orElse(null);
        this.disableReason = other.optionalDisableReason().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.itemListLocksId = other.optionalItemListLocksId().map(LongFilter::copy).orElse(null);
        this.tireBrandId = other.optionalTireBrandId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TireCriteria copy() {
        return new TireCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReference() {
        return reference;
    }

    public Optional<StringFilter> optionalReference() {
        return Optional.ofNullable(reference);
    }

    public StringFilter reference() {
        if (reference == null) {
            setReference(new StringFilter());
        }
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public StringFilter getTireWidth() {
        return tireWidth;
    }

    public Optional<StringFilter> optionalTireWidth() {
        return Optional.ofNullable(tireWidth);
    }

    public StringFilter tireWidth() {
        if (tireWidth == null) {
            setTireWidth(new StringFilter());
        }
        return tireWidth;
    }

    public void setTireWidth(StringFilter tireWidth) {
        this.tireWidth = tireWidth;
    }

    public StringFilter getTireHeight() {
        return tireHeight;
    }

    public Optional<StringFilter> optionalTireHeight() {
        return Optional.ofNullable(tireHeight);
    }

    public StringFilter tireHeight() {
        if (tireHeight == null) {
            setTireHeight(new StringFilter());
        }
        return tireHeight;
    }

    public void setTireHeight(StringFilter tireHeight) {
        this.tireHeight = tireHeight;
    }

    public StringFilter getTireDiameter() {
        return tireDiameter;
    }

    public Optional<StringFilter> optionalTireDiameter() {
        return Optional.ofNullable(tireDiameter);
    }

    public StringFilter tireDiameter() {
        if (tireDiameter == null) {
            setTireDiameter(new StringFilter());
        }
        return tireDiameter;
    }

    public void setTireDiameter(StringFilter tireDiameter) {
        this.tireDiameter = tireDiameter;
    }

    public TireTypeFilter getTireType() {
        return tireType;
    }

    public Optional<TireTypeFilter> optionalTireType() {
        return Optional.ofNullable(tireType);
    }

    public TireTypeFilter tireType() {
        if (tireType == null) {
            setTireType(new TireTypeFilter());
        }
        return tireType;
    }

    public void setTireType(TireTypeFilter tireType) {
        this.tireType = tireType;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public Optional<StringFilter> optionalImageUrl() {
        return Optional.ofNullable(imageUrl);
    }

    public StringFilter imageUrl() {
        if (imageUrl == null) {
            setImageUrl(new StringFilter());
        }
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SpeedIndexFilter getSpeedIndex() {
        return speedIndex;
    }

    public Optional<SpeedIndexFilter> optionalSpeedIndex() {
        return Optional.ofNullable(speedIndex);
    }

    public SpeedIndexFilter speedIndex() {
        if (speedIndex == null) {
            setSpeedIndex(new SpeedIndexFilter());
        }
        return speedIndex;
    }

    public void setSpeedIndex(SpeedIndexFilter speedIndex) {
        this.speedIndex = speedIndex;
    }

    public ChargeIndexFilter getWeightIndex() {
        return weightIndex;
    }

    public Optional<ChargeIndexFilter> optionalWeightIndex() {
        return Optional.ofNullable(weightIndex);
    }

    public ChargeIndexFilter weightIndex() {
        if (weightIndex == null) {
            setWeightIndex(new ChargeIndexFilter());
        }
        return weightIndex;
    }

    public void setWeightIndex(ChargeIndexFilter weightIndex) {
        this.weightIndex = weightIndex;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BooleanFilter getDisable() {
        return disable;
    }

    public Optional<BooleanFilter> optionalDisable() {
        return Optional.ofNullable(disable);
    }

    public BooleanFilter disable() {
        if (disable == null) {
            setDisable(new BooleanFilter());
        }
        return disable;
    }

    public void setDisable(BooleanFilter disable) {
        this.disable = disable;
    }

    public StringFilter getDisableReason() {
        return disableReason;
    }

    public Optional<StringFilter> optionalDisableReason() {
        return Optional.ofNullable(disableReason);
    }

    public StringFilter disableReason() {
        if (disableReason == null) {
            setDisableReason(new StringFilter());
        }
        return disableReason;
    }

    public void setDisableReason(StringFilter disableReason) {
        this.disableReason = disableReason;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getItemListLocksId() {
        return itemListLocksId;
    }

    public Optional<LongFilter> optionalItemListLocksId() {
        return Optional.ofNullable(itemListLocksId);
    }

    public LongFilter itemListLocksId() {
        if (itemListLocksId == null) {
            setItemListLocksId(new LongFilter());
        }
        return itemListLocksId;
    }

    public void setItemListLocksId(LongFilter itemListLocksId) {
        this.itemListLocksId = itemListLocksId;
    }

    public LongFilter getTireBrandId() {
        return tireBrandId;
    }

    public Optional<LongFilter> optionalTireBrandId() {
        return Optional.ofNullable(tireBrandId);
    }

    public LongFilter tireBrandId() {
        if (tireBrandId == null) {
            setTireBrandId(new LongFilter());
        }
        return tireBrandId;
    }

    public void setTireBrandId(LongFilter tireBrandId) {
        this.tireBrandId = tireBrandId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TireCriteria that = (TireCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(name, that.name) &&
            Objects.equals(price, that.price) &&
            Objects.equals(tireWidth, that.tireWidth) &&
            Objects.equals(tireHeight, that.tireHeight) &&
            Objects.equals(tireDiameter, that.tireDiameter) &&
            Objects.equals(tireType, that.tireType) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(speedIndex, that.speedIndex) &&
            Objects.equals(weightIndex, that.weightIndex) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(disable, that.disable) &&
            Objects.equals(disableReason, that.disableReason) &&
            Objects.equals(description, that.description) &&
            Objects.equals(itemListLocksId, that.itemListLocksId) &&
            Objects.equals(tireBrandId, that.tireBrandId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reference,
            name,
            price,
            tireWidth,
            tireHeight,
            tireDiameter,
            tireType,
            imageUrl,
            speedIndex,
            weightIndex,
            quantity,
            disable,
            disableReason,
            description,
            itemListLocksId,
            tireBrandId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TireCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalReference().map(f -> "reference=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalTireWidth().map(f -> "tireWidth=" + f + ", ").orElse("") +
            optionalTireHeight().map(f -> "tireHeight=" + f + ", ").orElse("") +
            optionalTireDiameter().map(f -> "tireDiameter=" + f + ", ").orElse("") +
            optionalTireType().map(f -> "tireType=" + f + ", ").orElse("") +
            optionalImageUrl().map(f -> "imageUrl=" + f + ", ").orElse("") +
            optionalSpeedIndex().map(f -> "speedIndex=" + f + ", ").orElse("") +
            optionalWeightIndex().map(f -> "weightIndex=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalDisable().map(f -> "disable=" + f + ", ").orElse("") +
            optionalDisableReason().map(f -> "disableReason=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalItemListLocksId().map(f -> "itemListLocksId=" + f + ", ").orElse("") +
            optionalTireBrandId().map(f -> "tireBrandId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}

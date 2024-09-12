package fr.ecom.mstr.tire.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TireCriteriaTest {

    @Test
    void newTireCriteriaHasAllFiltersNullTest() {
        var tireCriteria = new TireCriteria();
        assertThat(tireCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void tireCriteriaFluentMethodsCreatesFiltersTest() {
        var tireCriteria = new TireCriteria();

        setAllFilters(tireCriteria);

        assertThat(tireCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void tireCriteriaCopyCreatesNullFilterTest() {
        var tireCriteria = new TireCriteria();
        var copy = tireCriteria.copy();

        assertThat(tireCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(tireCriteria)
        );
    }

    @Test
    void tireCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tireCriteria = new TireCriteria();
        setAllFilters(tireCriteria);

        var copy = tireCriteria.copy();

        assertThat(tireCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(tireCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tireCriteria = new TireCriteria();

        assertThat(tireCriteria).hasToString("TireCriteria{}");
    }

    private static void setAllFilters(TireCriteria tireCriteria) {
        tireCriteria.id();
        tireCriteria.reference();
        tireCriteria.name();
        tireCriteria.price();
        tireCriteria.tireWidth();
        tireCriteria.tireHeight();
        tireCriteria.tireDiameter();
        tireCriteria.tireType();
        tireCriteria.imageUrl();
        tireCriteria.speedIndex();
        tireCriteria.weightIndex();
        tireCriteria.quantity();
        tireCriteria.disable();
        tireCriteria.disableReason();
        tireCriteria.description();
        tireCriteria.itemListLocksId();
        tireCriteria.tireBrandId();
        tireCriteria.distinct();
    }

    private static Condition<TireCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getReference()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getTireWidth()) &&
                condition.apply(criteria.getTireHeight()) &&
                condition.apply(criteria.getTireDiameter()) &&
                condition.apply(criteria.getTireType()) &&
                condition.apply(criteria.getImageUrl()) &&
                condition.apply(criteria.getSpeedIndex()) &&
                condition.apply(criteria.getWeightIndex()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getDisable()) &&
                condition.apply(criteria.getDisableReason()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getItemListLocksId()) &&
                condition.apply(criteria.getTireBrandId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TireCriteria> copyFiltersAre(TireCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getReference(), copy.getReference()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getTireWidth(), copy.getTireWidth()) &&
                condition.apply(criteria.getTireHeight(), copy.getTireHeight()) &&
                condition.apply(criteria.getTireDiameter(), copy.getTireDiameter()) &&
                condition.apply(criteria.getTireType(), copy.getTireType()) &&
                condition.apply(criteria.getImageUrl(), copy.getImageUrl()) &&
                condition.apply(criteria.getSpeedIndex(), copy.getSpeedIndex()) &&
                condition.apply(criteria.getWeightIndex(), copy.getWeightIndex()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getDisable(), copy.getDisable()) &&
                condition.apply(criteria.getDisableReason(), copy.getDisableReason()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getItemListLocksId(), copy.getItemListLocksId()) &&
                condition.apply(criteria.getTireBrandId(), copy.getTireBrandId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}

package fr.ecom.mstr.tire.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TireBrandAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTireBrandAllPropertiesEquals(TireBrand expected, TireBrand actual) {
        assertTireBrandAutoGeneratedPropertiesEquals(expected, actual);
        assertTireBrandAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTireBrandAllUpdatablePropertiesEquals(TireBrand expected, TireBrand actual) {
        assertTireBrandUpdatableFieldsEquals(expected, actual);
        assertTireBrandUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTireBrandAutoGeneratedPropertiesEquals(TireBrand expected, TireBrand actual) {
        assertThat(expected)
            .as("Verify TireBrand auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTireBrandUpdatableFieldsEquals(TireBrand expected, TireBrand actual) {
        assertThat(expected)
            .as("Verify TireBrand relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getLogoUrl()).as("check logoUrl").isEqualTo(actual.getLogoUrl()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTireBrandUpdatableRelationshipsEquals(TireBrand expected, TireBrand actual) {
        // empty method
    }
}

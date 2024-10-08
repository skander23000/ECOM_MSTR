package fr.ecom.mstr.tire.domain;

import static fr.ecom.mstr.tire.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerOrderAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerOrderAllPropertiesEquals(CustomerOrder expected, CustomerOrder actual) {
        assertCustomerOrderAutoGeneratedPropertiesEquals(expected, actual);
        assertCustomerOrderAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerOrderAllUpdatablePropertiesEquals(CustomerOrder expected, CustomerOrder actual) {
        assertCustomerOrderUpdatableFieldsEquals(expected, actual);
        assertCustomerOrderUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerOrderAutoGeneratedPropertiesEquals(CustomerOrder expected, CustomerOrder actual) {
        assertThat(expected)
            .as("Verify CustomerOrder auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerOrderUpdatableFieldsEquals(CustomerOrder expected, CustomerOrder actual) {
        assertThat(expected)
            .as("Verify CustomerOrder relevant properties")
            .satisfies(e -> assertThat(e.getOrderDate()).as("check orderDate").isEqualTo(actual.getOrderDate()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e ->
                assertThat(e.getTotalAmount())
                    .as("check totalAmount")
                    .usingComparator(bigDecimalCompareTo)
                    .isEqualTo(actual.getTotalAmount())
            )
            .satisfies(e -> assertThat(e.getPaymentDate()).as("check paymentDate").isEqualTo(actual.getPaymentDate()))
            .satisfies(e -> assertThat(e.getPaymentMethod()).as("check paymentMethod").isEqualTo(actual.getPaymentMethod()))
            .satisfies(e -> assertThat(e.getPaymentStatus()).as("check paymentStatus").isEqualTo(actual.getPaymentStatus()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCustomerOrderUpdatableRelationshipsEquals(CustomerOrder expected, CustomerOrder actual) {
        // empty method
    }
}

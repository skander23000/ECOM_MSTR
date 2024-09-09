package fr.ecom.mstr.tire.domain;

import static fr.ecom.mstr.tire.domain.CustomerOrderTestSamples.*;
import static fr.ecom.mstr.tire.domain.CustomerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.ecom.mstr.tire.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void customerOrderTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        CustomerOrder customerOrderBack = getCustomerOrderRandomSampleGenerator();

        customer.setCustomerOrder(customerOrderBack);
        assertThat(customer.getCustomerOrder()).isEqualTo(customerOrderBack);

        customer.customerOrder(null);
        assertThat(customer.getCustomerOrder()).isNull();
    }
}

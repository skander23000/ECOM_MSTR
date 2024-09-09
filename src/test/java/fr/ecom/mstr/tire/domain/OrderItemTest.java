package fr.ecom.mstr.tire.domain;

import static fr.ecom.mstr.tire.domain.CustomerOrderTestSamples.*;
import static fr.ecom.mstr.tire.domain.OrderItemTestSamples.*;
import static fr.ecom.mstr.tire.domain.TireTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.ecom.mstr.tire.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderItem.class);
        OrderItem orderItem1 = getOrderItemSample1();
        OrderItem orderItem2 = new OrderItem();
        assertThat(orderItem1).isNotEqualTo(orderItem2);

        orderItem2.setId(orderItem1.getId());
        assertThat(orderItem1).isEqualTo(orderItem2);

        orderItem2 = getOrderItemSample2();
        assertThat(orderItem1).isNotEqualTo(orderItem2);
    }

    @Test
    void customerOrderTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        CustomerOrder customerOrderBack = getCustomerOrderRandomSampleGenerator();

        orderItem.setCustomerOrder(customerOrderBack);
        assertThat(orderItem.getCustomerOrder()).isEqualTo(customerOrderBack);

        orderItem.customerOrder(null);
        assertThat(orderItem.getCustomerOrder()).isNull();
    }

    @Test
    void tireTest() {
        OrderItem orderItem = getOrderItemRandomSampleGenerator();
        Tire tireBack = getTireRandomSampleGenerator();

        orderItem.setTire(tireBack);
        assertThat(orderItem.getTire()).isEqualTo(tireBack);

        orderItem.tire(null);
        assertThat(orderItem.getTire()).isNull();
    }
}

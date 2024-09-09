package fr.ecom.mstr.tire.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CustomerOrder getCustomerOrderSample1() {
        return new CustomerOrder().id(1L);
    }

    public static CustomerOrder getCustomerOrderSample2() {
        return new CustomerOrder().id(2L);
    }

    public static CustomerOrder getCustomerOrderRandomSampleGenerator() {
        return new CustomerOrder().id(longCount.incrementAndGet());
    }
}

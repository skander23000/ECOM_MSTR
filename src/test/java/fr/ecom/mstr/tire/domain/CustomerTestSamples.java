package fr.ecom.mstr.tire.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .address("address1")
            .city("city1")
            .zipCode("zipCode1")
            .country("country1")
            .phoneNumber("phoneNumber1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .address("address2")
            .city("city2")
            .zipCode("zipCode2")
            .country("country2")
            .phoneNumber("phoneNumber2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .zipCode(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}

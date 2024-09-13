package fr.ecom.mstr.tire.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TireTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Tire getTireSample1() {
        return new Tire()
            .id(1L)
            .reference("reference1")
            .name("name1")
            .imageUrl("imageUrl1")
            .quantity(1)
            .disableReason("disableReason1")
            .description("description1");
    }

    public static Tire getTireSample2() {
        return new Tire()
            .id(2L)
            .reference("reference2")
            .name("name2")
            .imageUrl("imageUrl2")
            .quantity(2)
            .disableReason("disableReason2")
            .description("description2");
    }

    public static Tire getTireRandomSampleGenerator() {
        return new Tire()
            .id(longCount.incrementAndGet())
            .reference(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .imageUrl(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet())
            .disableReason(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}

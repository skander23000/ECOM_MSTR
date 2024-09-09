package fr.ecom.mstr.tire.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TireBrandTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TireBrand getTireBrandSample1() {
        return new TireBrand().id(1L).name("name1").logoUrl("logoUrl1");
    }

    public static TireBrand getTireBrandSample2() {
        return new TireBrand().id(2L).name("name2").logoUrl("logoUrl2");
    }

    public static TireBrand getTireBrandRandomSampleGenerator() {
        return new TireBrand().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).logoUrl(UUID.randomUUID().toString());
    }
}

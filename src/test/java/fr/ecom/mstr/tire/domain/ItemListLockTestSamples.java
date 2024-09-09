package fr.ecom.mstr.tire.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ItemListLockTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ItemListLock getItemListLockSample1() {
        return new ItemListLock().id(1L).userUuid(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).quantity(1);
    }

    public static ItemListLock getItemListLockSample2() {
        return new ItemListLock().id(2L).userUuid(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).quantity(2);
    }

    public static ItemListLock getItemListLockRandomSampleGenerator() {
        return new ItemListLock().id(longCount.incrementAndGet()).userUuid(UUID.randomUUID()).quantity(intCount.incrementAndGet());
    }
}

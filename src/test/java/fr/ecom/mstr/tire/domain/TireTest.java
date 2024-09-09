package fr.ecom.mstr.tire.domain;

import static fr.ecom.mstr.tire.domain.ItemListLockTestSamples.*;
import static fr.ecom.mstr.tire.domain.TireBrandTestSamples.*;
import static fr.ecom.mstr.tire.domain.TireTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.ecom.mstr.tire.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tire.class);
        Tire tire1 = getTireSample1();
        Tire tire2 = new Tire();
        assertThat(tire1).isNotEqualTo(tire2);

        tire2.setId(tire1.getId());
        assertThat(tire1).isEqualTo(tire2);

        tire2 = getTireSample2();
        assertThat(tire1).isNotEqualTo(tire2);
    }

    @Test
    void itemListLocksTest() {
        Tire tire = getTireRandomSampleGenerator();
        ItemListLock itemListLockBack = getItemListLockRandomSampleGenerator();

        tire.addItemListLocks(itemListLockBack);
        assertThat(tire.getItemListLocks()).containsOnly(itemListLockBack);
        assertThat(itemListLockBack.getTire()).isEqualTo(tire);

        tire.removeItemListLocks(itemListLockBack);
        assertThat(tire.getItemListLocks()).doesNotContain(itemListLockBack);
        assertThat(itemListLockBack.getTire()).isNull();

        tire.itemListLocks(new HashSet<>(Set.of(itemListLockBack)));
        assertThat(tire.getItemListLocks()).containsOnly(itemListLockBack);
        assertThat(itemListLockBack.getTire()).isEqualTo(tire);

        tire.setItemListLocks(new HashSet<>());
        assertThat(tire.getItemListLocks()).doesNotContain(itemListLockBack);
        assertThat(itemListLockBack.getTire()).isNull();
    }

    @Test
    void tireBrandTest() {
        Tire tire = getTireRandomSampleGenerator();
        TireBrand tireBrandBack = getTireBrandRandomSampleGenerator();

        tire.setTireBrand(tireBrandBack);
        assertThat(tire.getTireBrand()).isEqualTo(tireBrandBack);

        tire.tireBrand(null);
        assertThat(tire.getTireBrand()).isNull();
    }
}

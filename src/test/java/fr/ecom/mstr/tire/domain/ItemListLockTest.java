package fr.ecom.mstr.tire.domain;

import static fr.ecom.mstr.tire.domain.ItemListLockTestSamples.*;
import static fr.ecom.mstr.tire.domain.TireTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.ecom.mstr.tire.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemListLockTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemListLock.class);
        ItemListLock itemListLock1 = getItemListLockSample1();
        ItemListLock itemListLock2 = new ItemListLock();
        assertThat(itemListLock1).isNotEqualTo(itemListLock2);

        itemListLock2.setId(itemListLock1.getId());
        assertThat(itemListLock1).isEqualTo(itemListLock2);

        itemListLock2 = getItemListLockSample2();
        assertThat(itemListLock1).isNotEqualTo(itemListLock2);
    }

    @Test
    void tireTest() {
        ItemListLock itemListLock = getItemListLockRandomSampleGenerator();
        Tire tireBack = getTireRandomSampleGenerator();

        itemListLock.setTire(tireBack);
        assertThat(itemListLock.getTire()).isEqualTo(tireBack);

        itemListLock.tire(null);
        assertThat(itemListLock.getTire()).isNull();
    }
}

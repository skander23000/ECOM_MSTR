package fr.ecom.mstr.tire.domain;

import static fr.ecom.mstr.tire.domain.TireBrandTestSamples.*;
import static fr.ecom.mstr.tire.domain.TireTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.ecom.mstr.tire.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TireBrandTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TireBrand.class);
        TireBrand tireBrand1 = getTireBrandSample1();
        TireBrand tireBrand2 = new TireBrand();
        assertThat(tireBrand1).isNotEqualTo(tireBrand2);

        tireBrand2.setId(tireBrand1.getId());
        assertThat(tireBrand1).isEqualTo(tireBrand2);

        tireBrand2 = getTireBrandSample2();
        assertThat(tireBrand1).isNotEqualTo(tireBrand2);
    }

    @Test
    void tiresTest() {
        TireBrand tireBrand = getTireBrandRandomSampleGenerator();
        Tire tireBack = getTireRandomSampleGenerator();

        tireBrand.addTires(tireBack);
        assertThat(tireBrand.getTires()).containsOnly(tireBack);
        assertThat(tireBack.getTireBrand()).isEqualTo(tireBrand);

        tireBrand.removeTires(tireBack);
        assertThat(tireBrand.getTires()).doesNotContain(tireBack);
        assertThat(tireBack.getTireBrand()).isNull();

        tireBrand.tires(new HashSet<>(Set.of(tireBack)));
        assertThat(tireBrand.getTires()).containsOnly(tireBack);
        assertThat(tireBack.getTireBrand()).isEqualTo(tireBrand);

        tireBrand.setTires(new HashSet<>());
        assertThat(tireBrand.getTires()).doesNotContain(tireBack);
        assertThat(tireBack.getTireBrand()).isNull();
    }
}

package fr.ecom.mstr.tire.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.ecom.mstr.tire.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TireBrandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TireBrandDTO.class);
        TireBrandDTO tireBrandDTO1 = new TireBrandDTO();
        tireBrandDTO1.setId(1L);
        TireBrandDTO tireBrandDTO2 = new TireBrandDTO();
        assertThat(tireBrandDTO1).isNotEqualTo(tireBrandDTO2);
        tireBrandDTO2.setId(tireBrandDTO1.getId());
        assertThat(tireBrandDTO1).isEqualTo(tireBrandDTO2);
        tireBrandDTO2.setId(2L);
        assertThat(tireBrandDTO1).isNotEqualTo(tireBrandDTO2);
        tireBrandDTO1.setId(null);
        assertThat(tireBrandDTO1).isNotEqualTo(tireBrandDTO2);
    }
}

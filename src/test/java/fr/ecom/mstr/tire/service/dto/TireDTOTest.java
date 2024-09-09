package fr.ecom.mstr.tire.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.ecom.mstr.tire.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TireDTO.class);
        TireDTO tireDTO1 = new TireDTO();
        tireDTO1.setId(1L);
        TireDTO tireDTO2 = new TireDTO();
        assertThat(tireDTO1).isNotEqualTo(tireDTO2);
        tireDTO2.setId(tireDTO1.getId());
        assertThat(tireDTO1).isEqualTo(tireDTO2);
        tireDTO2.setId(2L);
        assertThat(tireDTO1).isNotEqualTo(tireDTO2);
        tireDTO1.setId(null);
        assertThat(tireDTO1).isNotEqualTo(tireDTO2);
    }
}

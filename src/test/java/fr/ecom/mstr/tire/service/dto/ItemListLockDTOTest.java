package fr.ecom.mstr.tire.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.ecom.mstr.tire.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemListLockDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemListLockDTO.class);
        ItemListLockDTO itemListLockDTO1 = new ItemListLockDTO();
        itemListLockDTO1.setId(1L);
        ItemListLockDTO itemListLockDTO2 = new ItemListLockDTO();
        assertThat(itemListLockDTO1).isNotEqualTo(itemListLockDTO2);
        itemListLockDTO2.setId(itemListLockDTO1.getId());
        assertThat(itemListLockDTO1).isEqualTo(itemListLockDTO2);
        itemListLockDTO2.setId(2L);
        assertThat(itemListLockDTO1).isNotEqualTo(itemListLockDTO2);
        itemListLockDTO1.setId(null);
        assertThat(itemListLockDTO1).isNotEqualTo(itemListLockDTO2);
    }
}

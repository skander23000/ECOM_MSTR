package fr.ecom.mstr.tire.service.mapper;

import static fr.ecom.mstr.tire.domain.ItemListLockAsserts.*;
import static fr.ecom.mstr.tire.domain.ItemListLockTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemListLockMapperTest {

    private ItemListLockMapper itemListLockMapper;

    @BeforeEach
    void setUp() {
        itemListLockMapper = new ItemListLockMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getItemListLockSample1();
        var actual = itemListLockMapper.toEntity(itemListLockMapper.toDto(expected));
        assertItemListLockAllPropertiesEquals(expected, actual);
    }
}

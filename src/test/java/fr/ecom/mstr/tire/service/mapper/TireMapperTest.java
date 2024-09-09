package fr.ecom.mstr.tire.service.mapper;

import static fr.ecom.mstr.tire.domain.TireAsserts.*;
import static fr.ecom.mstr.tire.domain.TireTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TireMapperTest {

    private TireMapper tireMapper;

    @BeforeEach
    void setUp() {
        tireMapper = new TireMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTireSample1();
        var actual = tireMapper.toEntity(tireMapper.toDto(expected));
        assertTireAllPropertiesEquals(expected, actual);
    }
}

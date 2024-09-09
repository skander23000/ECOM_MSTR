package fr.ecom.mstr.tire.service.mapper;

import static fr.ecom.mstr.tire.domain.TireBrandAsserts.*;
import static fr.ecom.mstr.tire.domain.TireBrandTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TireBrandMapperTest {

    private TireBrandMapper tireBrandMapper;

    @BeforeEach
    void setUp() {
        tireBrandMapper = new TireBrandMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTireBrandSample1();
        var actual = tireBrandMapper.toEntity(tireBrandMapper.toDto(expected));
        assertTireBrandAllPropertiesEquals(expected, actual);
    }
}

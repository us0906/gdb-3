package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ZubehoerTypMapperTest {

    private ZubehoerTypMapper zubehoerTypMapper;

    @BeforeEach
    public void setUp() {
        zubehoerTypMapper = new ZubehoerTypMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(zubehoerTypMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(zubehoerTypMapper.fromId(null)).isNull();
    }
}

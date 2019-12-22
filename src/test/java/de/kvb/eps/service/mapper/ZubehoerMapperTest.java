package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ZubehoerMapperTest {

    private ZubehoerMapper zubehoerMapper;

    @BeforeEach
    public void setUp() {
        zubehoerMapper = new ZubehoerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(zubehoerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(zubehoerMapper.fromId(null)).isNull();
    }
}

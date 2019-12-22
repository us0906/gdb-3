package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class GeraetMapperTest {

    private GeraetMapper geraetMapper;

    @BeforeEach
    public void setUp() {
        geraetMapper = new GeraetMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(geraetMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(geraetMapper.fromId(null)).isNull();
    }
}

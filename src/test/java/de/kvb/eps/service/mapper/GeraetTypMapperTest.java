package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class GeraetTypMapperTest {

    private GeraetTypMapper geraetTypMapper;

    @BeforeEach
    public void setUp() {
        geraetTypMapper = new GeraetTypMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(geraetTypMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(geraetTypMapper.fromId(null)).isNull();
    }
}

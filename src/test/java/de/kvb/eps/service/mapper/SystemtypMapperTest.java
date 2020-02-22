package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SystemtypMapperTest {

    private SystemtypMapper systemtypMapper;

    @BeforeEach
    public void setUp() {
        systemtypMapper = new SystemtypMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(systemtypMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(systemtypMapper.fromId(null)).isNull();
    }
}

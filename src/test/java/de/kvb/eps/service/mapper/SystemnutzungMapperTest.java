package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class SystemnutzungMapperTest {

    private SystemnutzungMapper systemnutzungMapper;

    @BeforeEach
    public void setUp() {
        systemnutzungMapper = new SystemnutzungMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(systemnutzungMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(systemnutzungMapper.fromId(null)).isNull();
    }
}

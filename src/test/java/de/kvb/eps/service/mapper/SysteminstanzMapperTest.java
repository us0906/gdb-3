package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class SysteminstanzMapperTest {

    private SysteminstanzMapper systeminstanzMapper;

    @BeforeEach
    public void setUp() {
        systeminstanzMapper = new SysteminstanzMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(systeminstanzMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(systeminstanzMapper.fromId(null)).isNull();
    }
}

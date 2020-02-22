package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ArztMapperTest {

    private ArztMapper arztMapper;

    @BeforeEach
    public void setUp() {
        arztMapper = new ArztMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(arztMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(arztMapper.fromId(null)).isNull();
    }
}

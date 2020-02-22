package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HerstellerMapperTest {

    private HerstellerMapper herstellerMapper;

    @BeforeEach
    public void setUp() {
        herstellerMapper = new HerstellerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(herstellerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(herstellerMapper.fromId(null)).isNull();
    }
}

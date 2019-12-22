package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class BetreiberMapperTest {

    private BetreiberMapper betreiberMapper;

    @BeforeEach
    public void setUp() {
        betreiberMapper = new BetreiberMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(betreiberMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(betreiberMapper.fromId(null)).isNull();
    }
}

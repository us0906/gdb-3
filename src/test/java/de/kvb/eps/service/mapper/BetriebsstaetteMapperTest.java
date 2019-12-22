package de.kvb.eps.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class BetriebsstaetteMapperTest {

    private BetriebsstaetteMapper betriebsstaetteMapper;

    @BeforeEach
    public void setUp() {
        betriebsstaetteMapper = new BetriebsstaetteMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(betriebsstaetteMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(betriebsstaetteMapper.fromId(null)).isNull();
    }
}

package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class BetriebsstaetteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Betriebsstaette.class);
        Betriebsstaette betriebsstaette1 = new Betriebsstaette();
        betriebsstaette1.setId(1L);
        Betriebsstaette betriebsstaette2 = new Betriebsstaette();
        betriebsstaette2.setId(betriebsstaette1.getId());
        assertThat(betriebsstaette1).isEqualTo(betriebsstaette2);
        betriebsstaette2.setId(2L);
        assertThat(betriebsstaette1).isNotEqualTo(betriebsstaette2);
        betriebsstaette1.setId(null);
        assertThat(betriebsstaette1).isNotEqualTo(betriebsstaette2);
    }
}

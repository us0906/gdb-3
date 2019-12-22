package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class BetreiberTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Betreiber.class);
        Betreiber betreiber1 = new Betreiber();
        betreiber1.setId(1L);
        Betreiber betreiber2 = new Betreiber();
        betreiber2.setId(betreiber1.getId());
        assertThat(betreiber1).isEqualTo(betreiber2);
        betreiber2.setId(2L);
        assertThat(betreiber1).isNotEqualTo(betreiber2);
        betreiber1.setId(null);
        assertThat(betreiber1).isNotEqualTo(betreiber2);
    }
}

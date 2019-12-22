package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class HerstellerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hersteller.class);
        Hersteller hersteller1 = new Hersteller();
        hersteller1.setId(1L);
        Hersteller hersteller2 = new Hersteller();
        hersteller2.setId(hersteller1.getId());
        assertThat(hersteller1).isEqualTo(hersteller2);
        hersteller2.setId(2L);
        assertThat(hersteller1).isNotEqualTo(hersteller2);
        hersteller1.setId(null);
        assertThat(hersteller1).isNotEqualTo(hersteller2);
    }
}

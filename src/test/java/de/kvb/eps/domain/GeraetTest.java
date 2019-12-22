package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class GeraetTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Geraet.class);
        Geraet geraet1 = new Geraet();
        geraet1.setId(1L);
        Geraet geraet2 = new Geraet();
        geraet2.setId(geraet1.getId());
        assertThat(geraet1).isEqualTo(geraet2);
        geraet2.setId(2L);
        assertThat(geraet1).isNotEqualTo(geraet2);
        geraet1.setId(null);
        assertThat(geraet1).isNotEqualTo(geraet2);
    }
}

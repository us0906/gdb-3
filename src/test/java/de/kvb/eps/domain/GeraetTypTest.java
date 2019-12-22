package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class GeraetTypTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeraetTyp.class);
        GeraetTyp geraetTyp1 = new GeraetTyp();
        geraetTyp1.setId(1L);
        GeraetTyp geraetTyp2 = new GeraetTyp();
        geraetTyp2.setId(geraetTyp1.getId());
        assertThat(geraetTyp1).isEqualTo(geraetTyp2);
        geraetTyp2.setId(2L);
        assertThat(geraetTyp1).isNotEqualTo(geraetTyp2);
        geraetTyp1.setId(null);
        assertThat(geraetTyp1).isNotEqualTo(geraetTyp2);
    }
}

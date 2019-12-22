package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class ArztTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Arzt.class);
        Arzt arzt1 = new Arzt();
        arzt1.setId(1L);
        Arzt arzt2 = new Arzt();
        arzt2.setId(arzt1.getId());
        assertThat(arzt1).isEqualTo(arzt2);
        arzt2.setId(2L);
        assertThat(arzt1).isNotEqualTo(arzt2);
        arzt1.setId(null);
        assertThat(arzt1).isNotEqualTo(arzt2);
    }
}

package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class ZubehoerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Zubehoer.class);
        Zubehoer zubehoer1 = new Zubehoer();
        zubehoer1.setId(1L);
        Zubehoer zubehoer2 = new Zubehoer();
        zubehoer2.setId(zubehoer1.getId());
        assertThat(zubehoer1).isEqualTo(zubehoer2);
        zubehoer2.setId(2L);
        assertThat(zubehoer1).isNotEqualTo(zubehoer2);
        zubehoer1.setId(null);
        assertThat(zubehoer1).isNotEqualTo(zubehoer2);
    }
}

package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class ZubehoerTypTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZubehoerTyp.class);
        ZubehoerTyp zubehoerTyp1 = new ZubehoerTyp();
        zubehoerTyp1.setId(1L);
        ZubehoerTyp zubehoerTyp2 = new ZubehoerTyp();
        zubehoerTyp2.setId(zubehoerTyp1.getId());
        assertThat(zubehoerTyp1).isEqualTo(zubehoerTyp2);
        zubehoerTyp2.setId(2L);
        assertThat(zubehoerTyp1).isNotEqualTo(zubehoerTyp2);
        zubehoerTyp1.setId(null);
        assertThat(zubehoerTyp1).isNotEqualTo(zubehoerTyp2);
    }
}

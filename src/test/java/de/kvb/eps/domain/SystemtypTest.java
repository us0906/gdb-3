package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class SystemtypTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Systemtyp.class);
        Systemtyp systemtyp1 = new Systemtyp();
        systemtyp1.setId(1L);
        Systemtyp systemtyp2 = new Systemtyp();
        systemtyp2.setId(systemtyp1.getId());
        assertThat(systemtyp1).isEqualTo(systemtyp2);
        systemtyp2.setId(2L);
        assertThat(systemtyp1).isNotEqualTo(systemtyp2);
        systemtyp1.setId(null);
        assertThat(systemtyp1).isNotEqualTo(systemtyp2);
    }
}

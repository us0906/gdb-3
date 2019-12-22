package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class SystemnutzungTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Systemnutzung.class);
        Systemnutzung systemnutzung1 = new Systemnutzung();
        systemnutzung1.setId(1L);
        Systemnutzung systemnutzung2 = new Systemnutzung();
        systemnutzung2.setId(systemnutzung1.getId());
        assertThat(systemnutzung1).isEqualTo(systemnutzung2);
        systemnutzung2.setId(2L);
        assertThat(systemnutzung1).isNotEqualTo(systemnutzung2);
        systemnutzung1.setId(null);
        assertThat(systemnutzung1).isNotEqualTo(systemnutzung2);
    }
}

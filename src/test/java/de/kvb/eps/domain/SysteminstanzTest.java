package de.kvb.eps.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class SysteminstanzTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Systeminstanz.class);
        Systeminstanz systeminstanz1 = new Systeminstanz();
        systeminstanz1.setId(1L);
        Systeminstanz systeminstanz2 = new Systeminstanz();
        systeminstanz2.setId(systeminstanz1.getId());
        assertThat(systeminstanz1).isEqualTo(systeminstanz2);
        systeminstanz2.setId(2L);
        assertThat(systeminstanz1).isNotEqualTo(systeminstanz2);
        systeminstanz1.setId(null);
        assertThat(systeminstanz1).isNotEqualTo(systeminstanz2);
    }
}

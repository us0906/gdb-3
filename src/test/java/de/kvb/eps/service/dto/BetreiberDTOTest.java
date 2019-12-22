package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class BetreiberDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BetreiberDTO.class);
        BetreiberDTO betreiberDTO1 = new BetreiberDTO();
        betreiberDTO1.setId(1L);
        BetreiberDTO betreiberDTO2 = new BetreiberDTO();
        assertThat(betreiberDTO1).isNotEqualTo(betreiberDTO2);
        betreiberDTO2.setId(betreiberDTO1.getId());
        assertThat(betreiberDTO1).isEqualTo(betreiberDTO2);
        betreiberDTO2.setId(2L);
        assertThat(betreiberDTO1).isNotEqualTo(betreiberDTO2);
        betreiberDTO1.setId(null);
        assertThat(betreiberDTO1).isNotEqualTo(betreiberDTO2);
    }
}

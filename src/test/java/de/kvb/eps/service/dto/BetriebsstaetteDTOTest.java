package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class BetriebsstaetteDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BetriebsstaetteDTO.class);
        BetriebsstaetteDTO betriebsstaetteDTO1 = new BetriebsstaetteDTO();
        betriebsstaetteDTO1.setId(1L);
        BetriebsstaetteDTO betriebsstaetteDTO2 = new BetriebsstaetteDTO();
        assertThat(betriebsstaetteDTO1).isNotEqualTo(betriebsstaetteDTO2);
        betriebsstaetteDTO2.setId(betriebsstaetteDTO1.getId());
        assertThat(betriebsstaetteDTO1).isEqualTo(betriebsstaetteDTO2);
        betriebsstaetteDTO2.setId(2L);
        assertThat(betriebsstaetteDTO1).isNotEqualTo(betriebsstaetteDTO2);
        betriebsstaetteDTO1.setId(null);
        assertThat(betriebsstaetteDTO1).isNotEqualTo(betriebsstaetteDTO2);
    }
}

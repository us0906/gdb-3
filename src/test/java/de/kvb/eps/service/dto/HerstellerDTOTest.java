package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class HerstellerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HerstellerDTO.class);
        HerstellerDTO herstellerDTO1 = new HerstellerDTO();
        herstellerDTO1.setId(1L);
        HerstellerDTO herstellerDTO2 = new HerstellerDTO();
        assertThat(herstellerDTO1).isNotEqualTo(herstellerDTO2);
        herstellerDTO2.setId(herstellerDTO1.getId());
        assertThat(herstellerDTO1).isEqualTo(herstellerDTO2);
        herstellerDTO2.setId(2L);
        assertThat(herstellerDTO1).isNotEqualTo(herstellerDTO2);
        herstellerDTO1.setId(null);
        assertThat(herstellerDTO1).isNotEqualTo(herstellerDTO2);
    }
}

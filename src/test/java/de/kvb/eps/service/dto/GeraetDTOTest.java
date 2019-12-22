package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class GeraetDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeraetDTO.class);
        GeraetDTO geraetDTO1 = new GeraetDTO();
        geraetDTO1.setId(1L);
        GeraetDTO geraetDTO2 = new GeraetDTO();
        assertThat(geraetDTO1).isNotEqualTo(geraetDTO2);
        geraetDTO2.setId(geraetDTO1.getId());
        assertThat(geraetDTO1).isEqualTo(geraetDTO2);
        geraetDTO2.setId(2L);
        assertThat(geraetDTO1).isNotEqualTo(geraetDTO2);
        geraetDTO1.setId(null);
        assertThat(geraetDTO1).isNotEqualTo(geraetDTO2);
    }
}

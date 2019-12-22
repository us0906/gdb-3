package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class GeraetTypDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GeraetTypDTO.class);
        GeraetTypDTO geraetTypDTO1 = new GeraetTypDTO();
        geraetTypDTO1.setId(1L);
        GeraetTypDTO geraetTypDTO2 = new GeraetTypDTO();
        assertThat(geraetTypDTO1).isNotEqualTo(geraetTypDTO2);
        geraetTypDTO2.setId(geraetTypDTO1.getId());
        assertThat(geraetTypDTO1).isEqualTo(geraetTypDTO2);
        geraetTypDTO2.setId(2L);
        assertThat(geraetTypDTO1).isNotEqualTo(geraetTypDTO2);
        geraetTypDTO1.setId(null);
        assertThat(geraetTypDTO1).isNotEqualTo(geraetTypDTO2);
    }
}

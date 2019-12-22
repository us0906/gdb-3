package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class ArztDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArztDTO.class);
        ArztDTO arztDTO1 = new ArztDTO();
        arztDTO1.setId(1L);
        ArztDTO arztDTO2 = new ArztDTO();
        assertThat(arztDTO1).isNotEqualTo(arztDTO2);
        arztDTO2.setId(arztDTO1.getId());
        assertThat(arztDTO1).isEqualTo(arztDTO2);
        arztDTO2.setId(2L);
        assertThat(arztDTO1).isNotEqualTo(arztDTO2);
        arztDTO1.setId(null);
        assertThat(arztDTO1).isNotEqualTo(arztDTO2);
    }
}

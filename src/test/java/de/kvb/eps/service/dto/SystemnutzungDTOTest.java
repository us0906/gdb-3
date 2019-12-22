package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class SystemnutzungDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemnutzungDTO.class);
        SystemnutzungDTO systemnutzungDTO1 = new SystemnutzungDTO();
        systemnutzungDTO1.setId(1L);
        SystemnutzungDTO systemnutzungDTO2 = new SystemnutzungDTO();
        assertThat(systemnutzungDTO1).isNotEqualTo(systemnutzungDTO2);
        systemnutzungDTO2.setId(systemnutzungDTO1.getId());
        assertThat(systemnutzungDTO1).isEqualTo(systemnutzungDTO2);
        systemnutzungDTO2.setId(2L);
        assertThat(systemnutzungDTO1).isNotEqualTo(systemnutzungDTO2);
        systemnutzungDTO1.setId(null);
        assertThat(systemnutzungDTO1).isNotEqualTo(systemnutzungDTO2);
    }
}

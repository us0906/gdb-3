package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class SystemtypDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemtypDTO.class);
        SystemtypDTO systemtypDTO1 = new SystemtypDTO();
        systemtypDTO1.setId(1L);
        SystemtypDTO systemtypDTO2 = new SystemtypDTO();
        assertThat(systemtypDTO1).isNotEqualTo(systemtypDTO2);
        systemtypDTO2.setId(systemtypDTO1.getId());
        assertThat(systemtypDTO1).isEqualTo(systemtypDTO2);
        systemtypDTO2.setId(2L);
        assertThat(systemtypDTO1).isNotEqualTo(systemtypDTO2);
        systemtypDTO1.setId(null);
        assertThat(systemtypDTO1).isNotEqualTo(systemtypDTO2);
    }
}

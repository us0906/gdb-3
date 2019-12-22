package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class SysteminstanzDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SysteminstanzDTO.class);
        SysteminstanzDTO systeminstanzDTO1 = new SysteminstanzDTO();
        systeminstanzDTO1.setId(1L);
        SysteminstanzDTO systeminstanzDTO2 = new SysteminstanzDTO();
        assertThat(systeminstanzDTO1).isNotEqualTo(systeminstanzDTO2);
        systeminstanzDTO2.setId(systeminstanzDTO1.getId());
        assertThat(systeminstanzDTO1).isEqualTo(systeminstanzDTO2);
        systeminstanzDTO2.setId(2L);
        assertThat(systeminstanzDTO1).isNotEqualTo(systeminstanzDTO2);
        systeminstanzDTO1.setId(null);
        assertThat(systeminstanzDTO1).isNotEqualTo(systeminstanzDTO2);
    }
}

package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class ZubehoerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZubehoerDTO.class);
        ZubehoerDTO zubehoerDTO1 = new ZubehoerDTO();
        zubehoerDTO1.setId(1L);
        ZubehoerDTO zubehoerDTO2 = new ZubehoerDTO();
        assertThat(zubehoerDTO1).isNotEqualTo(zubehoerDTO2);
        zubehoerDTO2.setId(zubehoerDTO1.getId());
        assertThat(zubehoerDTO1).isEqualTo(zubehoerDTO2);
        zubehoerDTO2.setId(2L);
        assertThat(zubehoerDTO1).isNotEqualTo(zubehoerDTO2);
        zubehoerDTO1.setId(null);
        assertThat(zubehoerDTO1).isNotEqualTo(zubehoerDTO2);
    }
}

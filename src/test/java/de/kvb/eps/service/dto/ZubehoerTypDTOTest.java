package de.kvb.eps.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.kvb.eps.web.rest.TestUtil;

public class ZubehoerTypDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ZubehoerTypDTO.class);
        ZubehoerTypDTO zubehoerTypDTO1 = new ZubehoerTypDTO();
        zubehoerTypDTO1.setId(1L);
        ZubehoerTypDTO zubehoerTypDTO2 = new ZubehoerTypDTO();
        assertThat(zubehoerTypDTO1).isNotEqualTo(zubehoerTypDTO2);
        zubehoerTypDTO2.setId(zubehoerTypDTO1.getId());
        assertThat(zubehoerTypDTO1).isEqualTo(zubehoerTypDTO2);
        zubehoerTypDTO2.setId(2L);
        assertThat(zubehoerTypDTO1).isNotEqualTo(zubehoerTypDTO2);
        zubehoerTypDTO1.setId(null);
        assertThat(zubehoerTypDTO1).isNotEqualTo(zubehoerTypDTO2);
    }
}

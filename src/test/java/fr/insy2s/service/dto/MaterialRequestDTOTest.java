package fr.insy2s.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.insy2s.web.rest.TestUtil;

public class MaterialRequestDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialRequestDTO.class);
        MaterialRequestDTO materialRequestDTO1 = new MaterialRequestDTO();
        materialRequestDTO1.setId(1L);
        MaterialRequestDTO materialRequestDTO2 = new MaterialRequestDTO();
        assertThat(materialRequestDTO1).isNotEqualTo(materialRequestDTO2);
        materialRequestDTO2.setId(materialRequestDTO1.getId());
        assertThat(materialRequestDTO1).isEqualTo(materialRequestDTO2);
        materialRequestDTO2.setId(2L);
        assertThat(materialRequestDTO1).isNotEqualTo(materialRequestDTO2);
        materialRequestDTO1.setId(null);
        assertThat(materialRequestDTO1).isNotEqualTo(materialRequestDTO2);
    }
}

package fr.insy2s.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.insy2s.web.rest.TestUtil;

public class MaterialTypeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialTypeDTO.class);
        MaterialTypeDTO materialTypeDTO1 = new MaterialTypeDTO();
        materialTypeDTO1.setId(1L);
        MaterialTypeDTO materialTypeDTO2 = new MaterialTypeDTO();
        assertThat(materialTypeDTO1).isNotEqualTo(materialTypeDTO2);
        materialTypeDTO2.setId(materialTypeDTO1.getId());
        assertThat(materialTypeDTO1).isEqualTo(materialTypeDTO2);
        materialTypeDTO2.setId(2L);
        assertThat(materialTypeDTO1).isNotEqualTo(materialTypeDTO2);
        materialTypeDTO1.setId(null);
        assertThat(materialTypeDTO1).isNotEqualTo(materialTypeDTO2);
    }
}

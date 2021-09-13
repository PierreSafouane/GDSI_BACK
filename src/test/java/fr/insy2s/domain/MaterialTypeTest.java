package fr.insy2s.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.insy2s.web.rest.TestUtil;

public class MaterialTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialType.class);
        MaterialType materialType1 = new MaterialType();
        materialType1.setId(1L);
        MaterialType materialType2 = new MaterialType();
        materialType2.setId(materialType1.getId());
        assertThat(materialType1).isEqualTo(materialType2);
        materialType2.setId(2L);
        assertThat(materialType1).isNotEqualTo(materialType2);
        materialType1.setId(null);
        assertThat(materialType1).isNotEqualTo(materialType2);
    }
}

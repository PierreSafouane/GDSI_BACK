package fr.insy2s.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.insy2s.web.rest.TestUtil;

public class MaterialRequestTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialRequest.class);
        MaterialRequest materialRequest1 = new MaterialRequest();
        materialRequest1.setId(1L);
        MaterialRequest materialRequest2 = new MaterialRequest();
        materialRequest2.setId(materialRequest1.getId());
        assertThat(materialRequest1).isEqualTo(materialRequest2);
        materialRequest2.setId(2L);
        assertThat(materialRequest1).isNotEqualTo(materialRequest2);
        materialRequest1.setId(null);
        assertThat(materialRequest1).isNotEqualTo(materialRequest2);
    }
}

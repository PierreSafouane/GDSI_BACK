package fr.insy2s.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.insy2s.web.rest.TestUtil;

public class PresenceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presence.class);
        Presence presence1 = new Presence();
        presence1.setId(1L);
        Presence presence2 = new Presence();
        presence2.setId(presence1.getId());
        assertThat(presence1).isEqualTo(presence2);
        presence2.setId(2L);
        assertThat(presence1).isNotEqualTo(presence2);
        presence1.setId(null);
        assertThat(presence1).isNotEqualTo(presence2);
    }
}

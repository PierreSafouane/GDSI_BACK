package fr.insy2s.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.insy2s.web.rest.TestUtil;

public class RoomImageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomImage.class);
        RoomImage roomImage1 = new RoomImage();
        roomImage1.setId(1L);
        RoomImage roomImage2 = new RoomImage();
        roomImage2.setId(roomImage1.getId());
        assertThat(roomImage1).isEqualTo(roomImage2);
        roomImage2.setId(2L);
        assertThat(roomImage1).isNotEqualTo(roomImage2);
        roomImage1.setId(null);
        assertThat(roomImage1).isNotEqualTo(roomImage2);
    }
}

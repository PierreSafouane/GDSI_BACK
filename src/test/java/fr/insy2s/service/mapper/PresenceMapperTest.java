package fr.insy2s.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PresenceMapperTest {

    private PresenceMapper presenceMapper;

    @BeforeEach
    public void setUp() {
        presenceMapper = new PresenceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(presenceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(presenceMapper.fromId(null)).isNull();
    }
}

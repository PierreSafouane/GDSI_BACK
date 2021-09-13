package fr.insy2s.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class RoomImageMapperTest {

    private RoomImageMapper roomImageMapper;

    @BeforeEach
    public void setUp() {
        roomImageMapper = new RoomImageMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(roomImageMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(roomImageMapper.fromId(null)).isNull();
    }
}

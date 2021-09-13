package fr.insy2s.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BookingMapperTest {

    private BookingMapper bookingMapper;

    @BeforeEach
    public void setUp() {
        bookingMapper = new BookingMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bookingMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bookingMapper.fromId(null)).isNull();
    }
}

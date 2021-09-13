package fr.insy2s.service.mapper;


import fr.insy2s.domain.*;
import fr.insy2s.service.dto.BookingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring", uses = {RoomMapper.class})
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {

    @Mapping(source = "room.id", target = "roomId")
    BookingDTO toDto(Booking booking);

    @Mapping(target = "presences", ignore = true)
    @Mapping(target = "removePresences", ignore = true)
    @Mapping(source = "roomId", target = "room")
    Booking toEntity(BookingDTO bookingDTO);

    default Booking fromId(Long id) {
        if (id == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }
}

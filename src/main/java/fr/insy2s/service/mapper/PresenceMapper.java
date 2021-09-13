package fr.insy2s.service.mapper;


import fr.insy2s.domain.*;
import fr.insy2s.service.dto.PresenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Presence} and its DTO {@link PresenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, BookingMapper.class})
public interface PresenceMapper extends EntityMapper<PresenceDTO, Presence> {

    @Mapping(source = "appUser.id", target = "appUserId")
    @Mapping(source = "booking.id", target = "bookingId")
    PresenceDTO toDto(Presence presence);

    @Mapping(source = "appUserId", target = "appUser")
    @Mapping(source = "bookingId", target = "booking")
    Presence toEntity(PresenceDTO presenceDTO);

    default Presence fromId(Long id) {
        if (id == null) {
            return null;
        }
        Presence presence = new Presence();
        presence.setId(id);
        return presence;
    }
}

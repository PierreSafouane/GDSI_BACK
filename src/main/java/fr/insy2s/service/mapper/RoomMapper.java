package fr.insy2s.service.mapper;


import fr.insy2s.domain.*;
import fr.insy2s.service.dto.RoomDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {


    @Mapping(target = "images", ignore = true)
    @Mapping(target = "removeImages", ignore = true)
    @Mapping(target = "materials", ignore = true)
    @Mapping(target = "removeMaterials", ignore = true)
    Room toEntity(RoomDTO roomDTO);

    default Room fromId(Long id) {
        if (id == null) {
            return null;
        }
        Room room = new Room();
        room.setId(id);
        return room;
    }
}

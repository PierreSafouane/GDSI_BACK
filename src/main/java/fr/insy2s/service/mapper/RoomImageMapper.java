package fr.insy2s.service.mapper;


import fr.insy2s.domain.*;
import fr.insy2s.service.dto.RoomImageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RoomImage} and its DTO {@link RoomImageDTO}.
 */
@Mapper(componentModel = "spring", uses = {RoomMapper.class})
public interface RoomImageMapper extends EntityMapper<RoomImageDTO, RoomImage> {

    @Mapping(source = "room.id", target = "roomId")
    RoomImageDTO toDto(RoomImage roomImage);

    @Mapping(source = "roomId", target = "room")
    RoomImage toEntity(RoomImageDTO roomImageDTO);

    default RoomImage fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoomImage roomImage = new RoomImage();
        roomImage.setId(id);
        return roomImage;
    }
}

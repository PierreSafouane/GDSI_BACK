package fr.insy2s.service.mapper;


import fr.insy2s.domain.*;
import fr.insy2s.service.dto.MaterialDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Material} and its DTO {@link MaterialDTO}.
 */
@Mapper(componentModel = "spring", uses = {MaterialTypeMapper.class, RoomMapper.class})
public interface MaterialMapper extends EntityMapper<MaterialDTO, Material> {


    MaterialDTO toDto(Material material);

    
    Material toEntity(MaterialDTO materialDTO);

    default Material fromId(Long id) {
        if (id == null) {
            return null;
        }
        Material material = new Material();
        material.setId(id);
        return material;
    }
}

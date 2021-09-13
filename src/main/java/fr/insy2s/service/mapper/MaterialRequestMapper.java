package fr.insy2s.service.mapper;


import fr.insy2s.domain.*;
import fr.insy2s.service.dto.MaterialRequestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaterialRequest} and its DTO {@link MaterialRequestDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, MaterialMapper.class})
public interface MaterialRequestMapper extends EntityMapper<MaterialRequestDTO, MaterialRequest> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "material.id", target = "materialId")
    MaterialRequestDTO toDto(MaterialRequest materialRequest);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "materialId", target = "material")
    MaterialRequest toEntity(MaterialRequestDTO materialRequestDTO);

    default MaterialRequest fromId(Long id) {
        if (id == null) {
            return null;
        }
        MaterialRequest materialRequest = new MaterialRequest();
        materialRequest.setId(id);
        return materialRequest;
    }
}

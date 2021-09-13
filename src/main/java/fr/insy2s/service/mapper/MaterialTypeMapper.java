package fr.insy2s.service.mapper;


import fr.insy2s.domain.*;
import fr.insy2s.service.dto.MaterialTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MaterialType} and its DTO {@link MaterialTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MaterialTypeMapper extends EntityMapper<MaterialTypeDTO, MaterialType> {



    default MaterialType fromId(Long id) {
        if (id == null) {
            return null;
        }
        MaterialType materialType = new MaterialType();
        materialType.setId(id);
        return materialType;
    }
}

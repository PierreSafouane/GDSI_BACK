package fr.insy2s.service;

import fr.insy2s.service.dto.MaterialTypeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.insy2s.domain.MaterialType}.
 */
public interface MaterialTypeService {

    /**
     * Save a materialType.
     *
     * @param materialTypeDTO the entity to save.
     * @return the persisted entity.
     */
    MaterialTypeDTO save(MaterialTypeDTO materialTypeDTO);

    /**
     * Get all the materialTypes.
     *
     * @return the list of entities.
     */
    List<MaterialTypeDTO> findAll();


    /**
     * Get the "id" materialType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaterialTypeDTO> findOne(Long id);

    /**
     * Delete the "id" materialType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package fr.insy2s.service;

import fr.insy2s.service.dto.MaterialRequestDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.insy2s.domain.MaterialRequest}.
 */
public interface MaterialRequestService {

    /**
     * Save a materialRequest.
     *
     * @param materialRequestDTO the entity to save.
     * @return the persisted entity.
     */
    MaterialRequestDTO save(MaterialRequestDTO materialRequestDTO);

    /**
     * Get all the materialRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MaterialRequestDTO> findAll(Pageable pageable);


    /**
     * Get the "id" materialRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaterialRequestDTO> findOne(Long id);

    /**
     * Delete the "id" materialRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

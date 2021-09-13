package fr.insy2s.service;

import fr.insy2s.service.dto.RoomImageDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.insy2s.domain.RoomImage}.
 */
public interface RoomImageService {

    /**
     * Save a roomImage.
     *
     * @param roomImageDTO the entity to save.
     * @return the persisted entity.
     */
    RoomImageDTO save(RoomImageDTO roomImageDTO);

    /**
     * Get all the roomImages.
     *
     * @return the list of entities.
     */
    List<RoomImageDTO> findAll();


    /**
     * Get the "id" roomImage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RoomImageDTO> findOne(Long id);

    /**
     * Delete the "id" roomImage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

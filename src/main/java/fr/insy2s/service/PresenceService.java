package fr.insy2s.service;

import fr.insy2s.service.dto.PresenceDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.insy2s.domain.Presence}.
 */
public interface PresenceService {

    /**
     * Save a presence.
     *
     * @param presenceDTO the entity to save.
     * @return the persisted entity.
     */
    PresenceDTO save(PresenceDTO presenceDTO);

    /**
     * Get all the presences.
     *
     * @return the list of entities.
     */
    List<PresenceDTO> findAll();


    /**
     * Get the "id" presence.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PresenceDTO> findOne(Long id);

    /**
     * Delete the "id" presence.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package fr.insy2s.service;

import fr.insy2s.domain.Booking;
import fr.insy2s.domain.Presence;
import fr.insy2s.domain.User;
import fr.insy2s.service.dto.BookingDTO;
import fr.insy2s.service.dto.PresenceDTO;
import fr.insy2s.service.dto.UserDTO;

import java.time.Instant;
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

    /**
     * trouver tout les bookings ou le user y est invité
     *
     */
    List<BookingDTO> findBookingByUser(Long id);
    /**
     * find all presence by user
     *
     */
    List<Presence> findByAppUser(Long id);
    /**
     * find all presence by user before a given Date
     *
     */
    List<Presence> findByAppUserBeforeStartDate(Long id, Instant startDate);
    /**
     * find all presence by user before a given Date
     *
     */
    List<Presence> findByAppUserAfterEndDate(Long id, Instant startDate);
    /**
     * trouver tout les users invités dans le booking
     *
     */
    List<UserDTO> findUserByBooking(Long id);
}

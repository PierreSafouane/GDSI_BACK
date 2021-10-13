package fr.insy2s.web.rest;

import fr.insy2s.domain.Booking;
import fr.insy2s.domain.Presence;
import fr.insy2s.domain.User;
import fr.insy2s.service.PresenceService;
import fr.insy2s.service.dto.BookingDTO;
import fr.insy2s.service.dto.UserDTO;
import fr.insy2s.web.rest.errors.BadRequestAlertException;
import fr.insy2s.service.dto.PresenceDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.insy2s.domain.Presence}.
 */
@RestController
@RequestMapping("/api")
public class PresenceResource {

    private final Logger log = LoggerFactory.getLogger(PresenceResource.class);

    private static final String ENTITY_NAME = "presence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PresenceService presenceService;

    public PresenceResource(PresenceService presenceService) {
        this.presenceService = presenceService;
    }

    /**
     * {@code POST  /presences} : Create a new presence.
     *
     * @param presenceDTO the presenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new presenceDTO, or with status {@code 400 (Bad Request)} if the presence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/presences")
    public ResponseEntity<PresenceDTO> createPresence(@Valid @RequestBody PresenceDTO presenceDTO) throws URISyntaxException {
        log.debug("REST request to save Presence : {}", presenceDTO);
        if (presenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new presence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PresenceDTO result = presenceService.save(presenceDTO);
        return ResponseEntity.created(new URI("/api/presences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /presences} : Updates an existing presence.
     *
     * @param presenceDTO the presenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated presenceDTO,
     * or with status {@code 400 (Bad Request)} if the presenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the presenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/presences")
    public ResponseEntity<PresenceDTO> updatePresence(@Valid @RequestBody PresenceDTO presenceDTO) throws URISyntaxException {
        log.debug("REST request to update Presence : {}", presenceDTO);
        if (presenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PresenceDTO result = presenceService.save(presenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, presenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /presences} : get all the presences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of presences in body.
     */
    @GetMapping("/presences")
    public List<PresenceDTO> getAllPresences() {
        log.debug("REST request to get all Presences");
        return presenceService.findAll();
    }

    /**
     * {@code GET  /presences/:id} : get the "id" presence.
     *
     * @param id the id of the presenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the presenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/presences/{id}")
    public ResponseEntity<PresenceDTO> getPresence(@PathVariable Long id) {
        log.debug("REST request to get Presence : {}", id);
        Optional<PresenceDTO> presenceDTO = presenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(presenceDTO);
    }

    /**
     * {@code DELETE  /presences/:id} : delete the "id" presence.
     *
     * @param id the id of the presenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/presences/{id}")
    public ResponseEntity<Void> deletePresence(@PathVariable Long id) {
        log.debug("REST request to delete Presence : {}", id);
        presenceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * recuperation des bookings pour tel user
     *
     */
    @GetMapping("/presences/userbybooking/{id}")
    public List<UserDTO> presenceByBooking(@PathVariable Long id) {
        return presenceService.findUserByBooking(id);
    }

    /**
     * recuperation des users pour tel booking
     *
     */
    @GetMapping("/presences/bookingbyuser/{id}")
    public List<BookingDTO> bookingByUser(@PathVariable Long id) {
        return presenceService.findBookingByUser(id);
    }
    /**
     * recuperation des presences pour tel user
     *
     */
    @GetMapping("/presences/presencebyuser/{id}")
    public List<Presence> presenceByUser(@PathVariable Long id) {
        return presenceService.findByAppUser(id);
    }

    /**
     * recuperation des presences pour tel user avant la date actuelle
     *
     */
    @GetMapping("/presences/presenceByUserBeforeDate/{id}")
    public List<Presence> presenceByUserBeforeDate(@PathVariable Long id) {
        Instant actDate = Instant.now();
        return presenceService.findByAppUserBeforeStartDate(id,actDate);
    }
    /**
     * recuperation des presences pour tel user après la date actuelle
     *
     */
    @GetMapping("/presences/presenceByUserAfterDate/{id}")
    public List<Presence> presenceByUserAfterDate(@PathVariable Long id) {
        Instant actDate = Instant.now();
        return presenceService.findByAppUserAfterEndDate(id,actDate);
    }
}

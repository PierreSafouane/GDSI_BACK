package fr.insy2s.web.rest;

import fr.insy2s.service.RoomImageService;
import fr.insy2s.web.rest.errors.BadRequestAlertException;
import fr.insy2s.service.dto.RoomImageDTO;

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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.insy2s.domain.RoomImage}.
 */
@RestController
@RequestMapping("/api")
public class RoomImageResource {

    private final Logger log = LoggerFactory.getLogger(RoomImageResource.class);

    private static final String ENTITY_NAME = "roomImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomImageService roomImageService;

    public RoomImageResource(RoomImageService roomImageService) {
        this.roomImageService = roomImageService;
    }

    /**
     * {@code POST  /room-images} : Create a new roomImage.
     *
     * @param roomImageDTO the roomImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomImageDTO, or with status {@code 400 (Bad Request)} if the roomImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/room-images")
    public ResponseEntity<RoomImageDTO> createRoomImage(@Valid @RequestBody RoomImageDTO roomImageDTO) throws URISyntaxException {
        log.debug("REST request to save RoomImage : {}", roomImageDTO);
        if (roomImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new roomImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomImageDTO result = roomImageService.save(roomImageDTO);
        return ResponseEntity.created(new URI("/api/room-images/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /room-images} : Updates an existing roomImage.
     *
     * @param roomImageDTO the roomImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomImageDTO,
     * or with status {@code 400 (Bad Request)} if the roomImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/room-images")
    public ResponseEntity<RoomImageDTO> updateRoomImage(@Valid @RequestBody RoomImageDTO roomImageDTO) throws URISyntaxException {
        log.debug("REST request to update RoomImage : {}", roomImageDTO);
        if (roomImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RoomImageDTO result = roomImageService.save(roomImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomImageDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /room-images} : get all the roomImages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomImages in body.
     */
    @GetMapping("/room-images")
    public List<RoomImageDTO> getAllRoomImages() {
        log.debug("REST request to get all RoomImages");
        return roomImageService.findAll();
    }

    /**
     * {@code GET  /room-images/:id} : get the "id" roomImage.
     *
     * @param id the id of the roomImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/room-images/{id}")
    public ResponseEntity<RoomImageDTO> getRoomImage(@PathVariable Long id) {
        log.debug("REST request to get RoomImage : {}", id);
        Optional<RoomImageDTO> roomImageDTO = roomImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomImageDTO);
    }

    /**
     * {@code DELETE  /room-images/:id} : delete the "id" roomImage.
     *
     * @param id the id of the roomImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/room-images/{id}")
    public ResponseEntity<Void> deleteRoomImage(@PathVariable Long id) {
        log.debug("REST request to delete RoomImage : {}", id);
        roomImageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

package fr.insy2s.web.rest;

import fr.insy2s.domain.User;
import fr.insy2s.service.*;
import fr.insy2s.service.dto.PresenceDTO;
import fr.insy2s.web.rest.errors.BadRequestAlertException;
import fr.insy2s.service.dto.BookingDTO;
import fr.insy2s.service.dto.BookingCriteria;

import fr.insy2s.web.rest.vm.ManageBookingEmailVM;
import fr.insy2s.web.rest.vm.ManageBookingVM;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.insy2s.domain.Booking}.
 */
@RestController
@RequestMapping("/api")
public class BookingResource {

    private final Logger log = LoggerFactory.getLogger(BookingResource.class);

    private static final String ENTITY_NAME = "booking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingService bookingService;

    private final BookingQueryService bookingQueryService;

    private final MailService mailService;

    private final UserService userService;

    private  final PresenceService presenceService;

    public BookingResource(BookingService bookingService, BookingQueryService bookingQueryService, MailService mailService, UserService userService, PresenceService presenceService) {
        this.bookingService = bookingService;
        this.bookingQueryService = bookingQueryService;
        this.mailService = mailService;
        this.userService = userService;
        this.presenceService = presenceService;
    }

    /**
     * {@code POST  /bookings} : Create a new booking.
     *
     * @param manageBookingVM the bookingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingDTO, or with status {@code 400 (Bad Request)} if the booking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bookings")
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody ManageBookingVM manageBookingVM) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", manageBookingVM);
        if (manageBookingVM.getBookingDTO().getId() != null) {
            throw new BadRequestAlertException("A new booking cannot already have an ID", ENTITY_NAME, "idexists");
        }

        BookingDTO result = bookingService.save(manageBookingVM.getBookingDTO());
        PresenceDTO resultPresence = new PresenceDTO();

        for (int i = 0; i < manageBookingVM.getUsersGuestIds().size() ; i++) {
            PresenceDTO presenceDTO = new PresenceDTO();
            presenceDTO.setBookingId(result.getId());
            presenceDTO.setAppUserId(manageBookingVM.getUsersGuestIds().get(i));
            System.out.println("presence bookingId : "+presenceDTO.getBookingId()+", presence userId : "+presenceDTO.getAppUserId()+", presence boolean : "+presenceDTO.isIsAttending());
            resultPresence = presenceService.save(presenceDTO);
        }

        System.out.println("result presence after loop : "+resultPresence.getId());

        ManageBookingEmailVM manageBookingEmailVM = new ManageBookingEmailVM(manageBookingVM.getUserHostId(), manageBookingVM.getUsersGuestIds(), result.getId(), resultPresence.getId());
        sendMailToBookedUsers(manageBookingEmailVM);

        return ResponseEntity.created(new URI("/api/bookings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bookings} : Updates an existing booking.
     *
     * @param bookingDTO the bookingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingDTO,
     * or with status {@code 400 (Bad Request)} if the bookingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bookings")
    public ResponseEntity<BookingDTO> updateBooking(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to update Booking : {}", bookingDTO);
        if (bookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BookingDTO result = bookingService.save(bookingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bookings} : get all the bookings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookings in body.
     */
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings(BookingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Bookings by criteria: {}", criteria);
        Page<BookingDTO> page = bookingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bookings/count} : count all the bookings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bookings/count")
    public ResponseEntity<Long> countBookings(BookingCriteria criteria) {
        log.debug("REST request to count Bookings by criteria: {}", criteria);
        return ResponseEntity.ok().body(bookingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bookings/:id} : get the "id" booking.
     *
     * @param id the id of the bookingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        log.debug("REST request to get Booking : {}", id);
        Optional<BookingDTO> bookingDTO = bookingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingDTO);
    }

    /**
     * {@code DELETE  /bookings/:id} : delete the "id" booking.
     *
     * @param id the id of the bookingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.debug("REST request to delete Booking : {}", id);
        bookingService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


    /**
     * methode d'envoi de mail aux users invit?? a une booking, (se lance grace a la requette PostMapping de booking)
     * @param manageBookingEmailVM
     */
    @PostMapping("/bookings/mail")
    public void sendMailToBookedUsers(@RequestBody ManageBookingEmailVM manageBookingEmailVM) {
        System.out.println(manageBookingEmailVM.getReservationId());
        Optional<User> userHost = userService.getUserById(manageBookingEmailVM.getUserHostId());
        Optional<BookingDTO> reservation = bookingService.findOne(manageBookingEmailVM.getReservationId());
        for (int i = 0; i < manageBookingEmailVM.getUsersGuestIds().size(); i++) {
            Optional<User> userGuest = userService.getUserById(manageBookingEmailVM.getUsersGuestIds().get(i));
            mailService.sendEmailInvitation(userHost.get(), userGuest.get(), reservation.get(), manageBookingEmailVM.getPresenceId());
        }
    }
}

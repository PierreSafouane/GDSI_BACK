package fr.insy2s.web.rest;

import fr.insy2s.GdsiBackendApp;
import fr.insy2s.domain.Booking;
import fr.insy2s.domain.Presence;
import fr.insy2s.domain.Room;
import fr.insy2s.repository.BookingRepository;
import fr.insy2s.service.BookingService;
import fr.insy2s.service.dto.BookingDTO;
import fr.insy2s.service.mapper.BookingMapper;
import fr.insy2s.service.dto.BookingCriteria;
import fr.insy2s.service.BookingQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link BookingResource} REST controller.
 */
@SpringBootTest(classes = GdsiBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class BookingResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FINISH_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISH_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingQueryService bookingQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingMockMvc;

    private Booking booking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createEntity(EntityManager em) {
        Booking booking = new Booking()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .startAt(DEFAULT_START_AT)
            .finishAt(DEFAULT_FINISH_AT);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        booking.setRoom(room);
        return booking;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createUpdatedEntity(EntityManager em) {
        Booking booking = new Booking()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startAt(UPDATED_START_AT)
            .finishAt(UPDATED_FINISH_AT);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createUpdatedEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        booking.setRoom(room);
        return booking;
    }

    @BeforeEach
    public void initTest() {
        booking = createEntity(em);
    }

    @Test
    @Transactional
    public void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();
        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);
        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBooking.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBooking.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testBooking.getFinishAt()).isEqualTo(DEFAULT_FINISH_AT);
    }

    @Test
    @Transactional
    public void createBookingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking with an existing ID
        booking.setId(1L);
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setTitle(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);


        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setStartAt(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);


        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFinishAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setFinishAt(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);


        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList
        restBookingMockMvc.perform(get("/api/bookings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].finishAt").value(hasItem(DEFAULT_FINISH_AT.toString())));
    }
    
    @Test
    @Transactional
    public void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(booking.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.finishAt").value(DEFAULT_FINISH_AT.toString()));
    }


    @Test
    @Transactional
    public void getBookingsByIdFiltering() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        Long id = booking.getId();

        defaultBookingShouldBeFound("id.equals=" + id);
        defaultBookingShouldNotBeFound("id.notEquals=" + id);

        defaultBookingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultBookingShouldNotBeFound("id.greaterThan=" + id);

        defaultBookingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultBookingShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllBookingsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where title equals to DEFAULT_TITLE
        defaultBookingShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the bookingList where title equals to UPDATED_TITLE
        defaultBookingShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBookingsByTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where title not equals to DEFAULT_TITLE
        defaultBookingShouldNotBeFound("title.notEquals=" + DEFAULT_TITLE);

        // Get all the bookingList where title not equals to UPDATED_TITLE
        defaultBookingShouldBeFound("title.notEquals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBookingsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultBookingShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the bookingList where title equals to UPDATED_TITLE
        defaultBookingShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBookingsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where title is not null
        defaultBookingShouldBeFound("title.specified=true");

        // Get all the bookingList where title is null
        defaultBookingShouldNotBeFound("title.specified=false");
    }
                @Test
    @Transactional
    public void getAllBookingsByTitleContainsSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where title contains DEFAULT_TITLE
        defaultBookingShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the bookingList where title contains UPDATED_TITLE
        defaultBookingShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBookingsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where title does not contain DEFAULT_TITLE
        defaultBookingShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the bookingList where title does not contain UPDATED_TITLE
        defaultBookingShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }


    @Test
    @Transactional
    public void getAllBookingsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where description equals to DEFAULT_DESCRIPTION
        defaultBookingShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the bookingList where description equals to UPDATED_DESCRIPTION
        defaultBookingShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBookingsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where description not equals to DEFAULT_DESCRIPTION
        defaultBookingShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the bookingList where description not equals to UPDATED_DESCRIPTION
        defaultBookingShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBookingsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultBookingShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the bookingList where description equals to UPDATED_DESCRIPTION
        defaultBookingShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBookingsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where description is not null
        defaultBookingShouldBeFound("description.specified=true");

        // Get all the bookingList where description is null
        defaultBookingShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllBookingsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where description contains DEFAULT_DESCRIPTION
        defaultBookingShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the bookingList where description contains UPDATED_DESCRIPTION
        defaultBookingShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBookingsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where description does not contain DEFAULT_DESCRIPTION
        defaultBookingShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the bookingList where description does not contain UPDATED_DESCRIPTION
        defaultBookingShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllBookingsByStartAtIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startAt equals to DEFAULT_START_AT
        defaultBookingShouldBeFound("startAt.equals=" + DEFAULT_START_AT);

        // Get all the bookingList where startAt equals to UPDATED_START_AT
        defaultBookingShouldNotBeFound("startAt.equals=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    public void getAllBookingsByStartAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startAt not equals to DEFAULT_START_AT
        defaultBookingShouldNotBeFound("startAt.notEquals=" + DEFAULT_START_AT);

        // Get all the bookingList where startAt not equals to UPDATED_START_AT
        defaultBookingShouldBeFound("startAt.notEquals=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    public void getAllBookingsByStartAtIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startAt in DEFAULT_START_AT or UPDATED_START_AT
        defaultBookingShouldBeFound("startAt.in=" + DEFAULT_START_AT + "," + UPDATED_START_AT);

        // Get all the bookingList where startAt equals to UPDATED_START_AT
        defaultBookingShouldNotBeFound("startAt.in=" + UPDATED_START_AT);
    }

    @Test
    @Transactional
    public void getAllBookingsByStartAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where startAt is not null
        defaultBookingShouldBeFound("startAt.specified=true");

        // Get all the bookingList where startAt is null
        defaultBookingShouldNotBeFound("startAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByFinishAtIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where finishAt equals to DEFAULT_FINISH_AT
        defaultBookingShouldBeFound("finishAt.equals=" + DEFAULT_FINISH_AT);

        // Get all the bookingList where finishAt equals to UPDATED_FINISH_AT
        defaultBookingShouldNotBeFound("finishAt.equals=" + UPDATED_FINISH_AT);
    }

    @Test
    @Transactional
    public void getAllBookingsByFinishAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where finishAt not equals to DEFAULT_FINISH_AT
        defaultBookingShouldNotBeFound("finishAt.notEquals=" + DEFAULT_FINISH_AT);

        // Get all the bookingList where finishAt not equals to UPDATED_FINISH_AT
        defaultBookingShouldBeFound("finishAt.notEquals=" + UPDATED_FINISH_AT);
    }

    @Test
    @Transactional
    public void getAllBookingsByFinishAtIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where finishAt in DEFAULT_FINISH_AT or UPDATED_FINISH_AT
        defaultBookingShouldBeFound("finishAt.in=" + DEFAULT_FINISH_AT + "," + UPDATED_FINISH_AT);

        // Get all the bookingList where finishAt equals to UPDATED_FINISH_AT
        defaultBookingShouldNotBeFound("finishAt.in=" + UPDATED_FINISH_AT);
    }

    @Test
    @Transactional
    public void getAllBookingsByFinishAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where finishAt is not null
        defaultBookingShouldBeFound("finishAt.specified=true");

        // Get all the bookingList where finishAt is null
        defaultBookingShouldNotBeFound("finishAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllBookingsByPresencesIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        Presence presences = PresenceResourceIT.createEntity(em);
        em.persist(presences);
        em.flush();
        booking.addPresences(presences);
        bookingRepository.saveAndFlush(booking);
        Long presencesId = presences.getId();

        // Get all the bookingList where presences equals to presencesId
        defaultBookingShouldBeFound("presencesId.equals=" + presencesId);

        // Get all the bookingList where presences equals to presencesId + 1
        defaultBookingShouldNotBeFound("presencesId.equals=" + (presencesId + 1));
    }


    @Test
    @Transactional
    public void getAllBookingsByRoomIsEqualToSomething() throws Exception {
        // Get already existing entity
        Room room = booking.getRoom();
        bookingRepository.saveAndFlush(booking);
        Long roomId = room.getId();

        // Get all the bookingList where room equals to roomId
        defaultBookingShouldBeFound("roomId.equals=" + roomId);

        // Get all the bookingList where room equals to roomId + 1
        defaultBookingShouldNotBeFound("roomId.equals=" + (roomId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookingShouldBeFound(String filter) throws Exception {
        restBookingMockMvc.perform(get("/api/bookings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].finishAt").value(hasItem(DEFAULT_FINISH_AT.toString())));

        // Check, that the count call also returns 1
        restBookingMockMvc.perform(get("/api/bookings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookingShouldNotBeFound(String filter) throws Exception {
        restBookingMockMvc.perform(get("/api/bookings?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookingMockMvc.perform(get("/api/bookings/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking
        Booking updatedBooking = bookingRepository.findById(booking.getId()).get();
        // Disconnect from session so that the updates on updatedBooking are not directly saved in db
        em.detach(updatedBooking);
        updatedBooking
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .startAt(UPDATED_START_AT)
            .finishAt(UPDATED_FINISH_AT);
        BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

        restBookingMockMvc.perform(put("/api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBooking.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBooking.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testBooking.getFinishAt()).isEqualTo(UPDATED_FINISH_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc.perform(put("/api/bookings")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeDelete = bookingRepository.findAll().size();

        // Delete the booking
        restBookingMockMvc.perform(delete("/api/bookings/{id}", booking.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

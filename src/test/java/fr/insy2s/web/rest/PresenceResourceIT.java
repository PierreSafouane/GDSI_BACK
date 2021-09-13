package fr.insy2s.web.rest;

import fr.insy2s.GdsiBackendApp;
import fr.insy2s.domain.Presence;
import fr.insy2s.domain.User;
import fr.insy2s.domain.Booking;
import fr.insy2s.repository.PresenceRepository;
import fr.insy2s.service.PresenceService;
import fr.insy2s.service.dto.PresenceDTO;
import fr.insy2s.service.mapper.PresenceMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PresenceResource} REST controller.
 */
@SpringBootTest(classes = GdsiBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class PresenceResourceIT {

    private static final Boolean DEFAULT_IS_ATTENDING = false;
    private static final Boolean UPDATED_IS_ATTENDING = true;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private PresenceMapper presenceMapper;

    @Autowired
    private PresenceService presenceService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPresenceMockMvc;

    private Presence presence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presence createEntity(EntityManager em) {
        Presence presence = new Presence()
            .isAttending(DEFAULT_IS_ATTENDING);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        presence.setAppUser(user);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        presence.setBooking(booking);
        return presence;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presence createUpdatedEntity(EntityManager em) {
        Presence presence = new Presence()
            .isAttending(UPDATED_IS_ATTENDING);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        presence.setAppUser(user);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        presence.setBooking(booking);
        return presence;
    }

    @BeforeEach
    public void initTest() {
        presence = createEntity(em);
    }

    @Test
    @Transactional
    public void createPresence() throws Exception {
        int databaseSizeBeforeCreate = presenceRepository.findAll().size();
        // Create the Presence
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);
        restPresenceMockMvc.perform(post("/api/presences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(presenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeCreate + 1);
        Presence testPresence = presenceList.get(presenceList.size() - 1);
        assertThat(testPresence.isIsAttending()).isEqualTo(DEFAULT_IS_ATTENDING);
    }

    @Test
    @Transactional
    public void createPresenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = presenceRepository.findAll().size();

        // Create the Presence with an existing ID
        presence.setId(1L);
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresenceMockMvc.perform(post("/api/presences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(presenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPresences() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get all the presenceList
        restPresenceMockMvc.perform(get("/api/presences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presence.getId().intValue())))
            .andExpect(jsonPath("$.[*].isAttending").value(hasItem(DEFAULT_IS_ATTENDING.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPresence() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        // Get the presence
        restPresenceMockMvc.perform(get("/api/presences/{id}", presence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(presence.getId().intValue()))
            .andExpect(jsonPath("$.isAttending").value(DEFAULT_IS_ATTENDING.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingPresence() throws Exception {
        // Get the presence
        restPresenceMockMvc.perform(get("/api/presences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePresence() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();

        // Update the presence
        Presence updatedPresence = presenceRepository.findById(presence.getId()).get();
        // Disconnect from session so that the updates on updatedPresence are not directly saved in db
        em.detach(updatedPresence);
        updatedPresence
            .isAttending(UPDATED_IS_ATTENDING);
        PresenceDTO presenceDTO = presenceMapper.toDto(updatedPresence);

        restPresenceMockMvc.perform(put("/api/presences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(presenceDTO)))
            .andExpect(status().isOk());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
        Presence testPresence = presenceList.get(presenceList.size() - 1);
        assertThat(testPresence.isIsAttending()).isEqualTo(UPDATED_IS_ATTENDING);
    }

    @Test
    @Transactional
    public void updateNonExistingPresence() throws Exception {
        int databaseSizeBeforeUpdate = presenceRepository.findAll().size();

        // Create the Presence
        PresenceDTO presenceDTO = presenceMapper.toDto(presence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresenceMockMvc.perform(put("/api/presences")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(presenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Presence in the database
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePresence() throws Exception {
        // Initialize the database
        presenceRepository.saveAndFlush(presence);

        int databaseSizeBeforeDelete = presenceRepository.findAll().size();

        // Delete the presence
        restPresenceMockMvc.perform(delete("/api/presences/{id}", presence.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Presence> presenceList = presenceRepository.findAll();
        assertThat(presenceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

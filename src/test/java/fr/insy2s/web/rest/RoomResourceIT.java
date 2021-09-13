package fr.insy2s.web.rest;

import fr.insy2s.GdsiBackendApp;
import fr.insy2s.domain.Room;
import fr.insy2s.repository.RoomRepository;
import fr.insy2s.service.RoomService;
import fr.insy2s.service.dto.RoomDTO;
import fr.insy2s.service.mapper.RoomMapper;

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
 * Integration tests for the {@link RoomResource} REST controller.
 */
@SpringBootTest(classes = GdsiBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RoomResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_MAX_CAPACITY = 1;
    private static final Integer UPDATED_MAX_CAPACITY = 2;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private RoomService roomService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomMockMvc;

    private Room room;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createEntity(EntityManager em) {
        Room room = new Room()
            .name(DEFAULT_NAME)
            .maxCapacity(DEFAULT_MAX_CAPACITY);
        return room;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createUpdatedEntity(EntityManager em) {
        Room room = new Room()
            .name(UPDATED_NAME)
            .maxCapacity(UPDATED_MAX_CAPACITY);
        return room;
    }

    @BeforeEach
    public void initTest() {
        room = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoom() throws Exception {
        int databaseSizeBeforeCreate = roomRepository.findAll().size();
        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);
        restRoomMockMvc.perform(post("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isCreated());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate + 1);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoom.getMaxCapacity()).isEqualTo(DEFAULT_MAX_CAPACITY);
    }

    @Test
    @Transactional
    public void createRoomWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomRepository.findAll().size();

        // Create the Room with an existing ID
        room.setId(1L);
        RoomDTO roomDTO = roomMapper.toDto(room);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomMockMvc.perform(post("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRepository.findAll().size();
        // set the field null
        room.setName(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);


        restRoomMockMvc.perform(post("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMaxCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRepository.findAll().size();
        // set the field null
        room.setMaxCapacity(null);

        // Create the Room, which fails.
        RoomDTO roomDTO = roomMapper.toDto(room);


        restRoomMockMvc.perform(post("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRooms() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList
        restRoomMockMvc.perform(get("/api/rooms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].maxCapacity").value(hasItem(DEFAULT_MAX_CAPACITY)));
    }
    
    @Test
    @Transactional
    public void getRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get the room
        restRoomMockMvc.perform(get("/api/rooms/{id}", room.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(room.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.maxCapacity").value(DEFAULT_MAX_CAPACITY));
    }
    @Test
    @Transactional
    public void getNonExistingRoom() throws Exception {
        // Get the room
        restRoomMockMvc.perform(get("/api/rooms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room
        Room updatedRoom = roomRepository.findById(room.getId()).get();
        // Disconnect from session so that the updates on updatedRoom are not directly saved in db
        em.detach(updatedRoom);
        updatedRoom
            .name(UPDATED_NAME)
            .maxCapacity(UPDATED_MAX_CAPACITY);
        RoomDTO roomDTO = roomMapper.toDto(updatedRoom);

        restRoomMockMvc.perform(put("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoom.getMaxCapacity()).isEqualTo(UPDATED_MAX_CAPACITY);
    }

    @Test
    @Transactional
    public void updateNonExistingRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Create the Room
        RoomDTO roomDTO = roomMapper.toDto(room);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc.perform(put("/api/rooms")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeDelete = roomRepository.findAll().size();

        // Delete the room
        restRoomMockMvc.perform(delete("/api/rooms/{id}", room.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package fr.insy2s.web.rest;

import fr.insy2s.GdsiBackendApp;
import fr.insy2s.domain.RoomImage;
import fr.insy2s.domain.Room;
import fr.insy2s.repository.RoomImageRepository;
import fr.insy2s.service.RoomImageService;
import fr.insy2s.service.dto.RoomImageDTO;
import fr.insy2s.service.mapper.RoomImageMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RoomImageResource} REST controller.
 */
@SpringBootTest(classes = GdsiBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RoomImageResourceIT {

    private static final byte[] DEFAULT_IMAGE_ROOM = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_ROOM = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_ROOM_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_ROOM_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_PATH = "BBBBBBBBBB";

    @Autowired
    private RoomImageRepository roomImageRepository;

    @Autowired
    private RoomImageMapper roomImageMapper;

    @Autowired
    private RoomImageService roomImageService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomImageMockMvc;

    private RoomImage roomImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomImage createEntity(EntityManager em) {
        RoomImage roomImage = new RoomImage()
            .imageRoom(DEFAULT_IMAGE_ROOM)
            .imageRoomContentType(DEFAULT_IMAGE_ROOM_CONTENT_TYPE)
            .imagePath(DEFAULT_IMAGE_PATH);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        roomImage.setRoom(room);
        return roomImage;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomImage createUpdatedEntity(EntityManager em) {
        RoomImage roomImage = new RoomImage()
            .imageRoom(UPDATED_IMAGE_ROOM)
            .imageRoomContentType(UPDATED_IMAGE_ROOM_CONTENT_TYPE)
            .imagePath(UPDATED_IMAGE_PATH);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createUpdatedEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        roomImage.setRoom(room);
        return roomImage;
    }

    @BeforeEach
    public void initTest() {
        roomImage = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoomImage() throws Exception {
        int databaseSizeBeforeCreate = roomImageRepository.findAll().size();
        // Create the RoomImage
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);
        restRoomImageMockMvc.perform(post("/api/room-images")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomImageDTO)))
            .andExpect(status().isCreated());

        // Validate the RoomImage in the database
        List<RoomImage> roomImageList = roomImageRepository.findAll();
        assertThat(roomImageList).hasSize(databaseSizeBeforeCreate + 1);
        RoomImage testRoomImage = roomImageList.get(roomImageList.size() - 1);
        assertThat(testRoomImage.getImageRoom()).isEqualTo(DEFAULT_IMAGE_ROOM);
        assertThat(testRoomImage.getImageRoomContentType()).isEqualTo(DEFAULT_IMAGE_ROOM_CONTENT_TYPE);
        assertThat(testRoomImage.getImagePath()).isEqualTo(DEFAULT_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void createRoomImageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roomImageRepository.findAll().size();

        // Create the RoomImage with an existing ID
        roomImage.setId(1L);
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomImageMockMvc.perform(post("/api/room-images")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomImage in the database
        List<RoomImage> roomImageList = roomImageRepository.findAll();
        assertThat(roomImageList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRoomImages() throws Exception {
        // Initialize the database
        roomImageRepository.saveAndFlush(roomImage);

        // Get all the roomImageList
        restRoomImageMockMvc.perform(get("/api/room-images?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageRoomContentType").value(hasItem(DEFAULT_IMAGE_ROOM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageRoom").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_ROOM))))
            .andExpect(jsonPath("$.[*].imagePath").value(hasItem(DEFAULT_IMAGE_PATH)));
    }
    
    @Test
    @Transactional
    public void getRoomImage() throws Exception {
        // Initialize the database
        roomImageRepository.saveAndFlush(roomImage);

        // Get the roomImage
        restRoomImageMockMvc.perform(get("/api/room-images/{id}", roomImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomImage.getId().intValue()))
            .andExpect(jsonPath("$.imageRoomContentType").value(DEFAULT_IMAGE_ROOM_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageRoom").value(Base64Utils.encodeToString(DEFAULT_IMAGE_ROOM)))
            .andExpect(jsonPath("$.imagePath").value(DEFAULT_IMAGE_PATH));
    }
    @Test
    @Transactional
    public void getNonExistingRoomImage() throws Exception {
        // Get the roomImage
        restRoomImageMockMvc.perform(get("/api/room-images/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoomImage() throws Exception {
        // Initialize the database
        roomImageRepository.saveAndFlush(roomImage);

        int databaseSizeBeforeUpdate = roomImageRepository.findAll().size();

        // Update the roomImage
        RoomImage updatedRoomImage = roomImageRepository.findById(roomImage.getId()).get();
        // Disconnect from session so that the updates on updatedRoomImage are not directly saved in db
        em.detach(updatedRoomImage);
        updatedRoomImage
            .imageRoom(UPDATED_IMAGE_ROOM)
            .imageRoomContentType(UPDATED_IMAGE_ROOM_CONTENT_TYPE)
            .imagePath(UPDATED_IMAGE_PATH);
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(updatedRoomImage);

        restRoomImageMockMvc.perform(put("/api/room-images")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomImageDTO)))
            .andExpect(status().isOk());

        // Validate the RoomImage in the database
        List<RoomImage> roomImageList = roomImageRepository.findAll();
        assertThat(roomImageList).hasSize(databaseSizeBeforeUpdate);
        RoomImage testRoomImage = roomImageList.get(roomImageList.size() - 1);
        assertThat(testRoomImage.getImageRoom()).isEqualTo(UPDATED_IMAGE_ROOM);
        assertThat(testRoomImage.getImageRoomContentType()).isEqualTo(UPDATED_IMAGE_ROOM_CONTENT_TYPE);
        assertThat(testRoomImage.getImagePath()).isEqualTo(UPDATED_IMAGE_PATH);
    }

    @Test
    @Transactional
    public void updateNonExistingRoomImage() throws Exception {
        int databaseSizeBeforeUpdate = roomImageRepository.findAll().size();

        // Create the RoomImage
        RoomImageDTO roomImageDTO = roomImageMapper.toDto(roomImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomImageMockMvc.perform(put("/api/room-images")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(roomImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoomImage in the database
        List<RoomImage> roomImageList = roomImageRepository.findAll();
        assertThat(roomImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoomImage() throws Exception {
        // Initialize the database
        roomImageRepository.saveAndFlush(roomImage);

        int databaseSizeBeforeDelete = roomImageRepository.findAll().size();

        // Delete the roomImage
        restRoomImageMockMvc.perform(delete("/api/room-images/{id}", roomImage.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoomImage> roomImageList = roomImageRepository.findAll();
        assertThat(roomImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

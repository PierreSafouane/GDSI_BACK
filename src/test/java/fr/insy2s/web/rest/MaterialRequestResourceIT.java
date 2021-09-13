package fr.insy2s.web.rest;

import fr.insy2s.GdsiBackendApp;
import fr.insy2s.domain.MaterialRequest;
import fr.insy2s.domain.User;
import fr.insy2s.domain.Material;
import fr.insy2s.repository.MaterialRequestRepository;
import fr.insy2s.service.MaterialRequestService;
import fr.insy2s.service.dto.MaterialRequestDTO;
import fr.insy2s.service.mapper.MaterialRequestMapper;
import fr.insy2s.service.dto.MaterialRequestCriteria;
import fr.insy2s.service.MaterialRequestQueryService;

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
 * Integration tests for the {@link MaterialRequestResource} REST controller.
 */
@SpringBootTest(classes = GdsiBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MaterialRequestResourceIT {

    private static final Instant DEFAULT_DATE_REQUEST = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_REQUEST = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_VALIDATED = false;
    private static final Boolean UPDATED_VALIDATED = true;

    private static final Integer DEFAULT_QUANTITY_REQUESTED = 1;
    private static final Integer UPDATED_QUANTITY_REQUESTED = 2;
    private static final Integer SMALLER_QUANTITY_REQUESTED = 1 - 1;

    @Autowired
    private MaterialRequestRepository materialRequestRepository;

    @Autowired
    private MaterialRequestMapper materialRequestMapper;

    @Autowired
    private MaterialRequestService materialRequestService;

    @Autowired
    private MaterialRequestQueryService materialRequestQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialRequestMockMvc;

    private MaterialRequest materialRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialRequest createEntity(EntityManager em) {
        MaterialRequest materialRequest = new MaterialRequest()
            .dateRequest(DEFAULT_DATE_REQUEST)
            .validated(DEFAULT_VALIDATED)
            .quantityRequested(DEFAULT_QUANTITY_REQUESTED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        materialRequest.setUser(user);
        // Add required entity
        Material material;
        if (TestUtil.findAll(em, Material.class).isEmpty()) {
            material = MaterialResourceIT.createEntity(em);
            em.persist(material);
            em.flush();
        } else {
            material = TestUtil.findAll(em, Material.class).get(0);
        }
        materialRequest.setMaterial(material);
        return materialRequest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialRequest createUpdatedEntity(EntityManager em) {
        MaterialRequest materialRequest = new MaterialRequest()
            .dateRequest(UPDATED_DATE_REQUEST)
            .validated(UPDATED_VALIDATED)
            .quantityRequested(UPDATED_QUANTITY_REQUESTED);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        materialRequest.setUser(user);
        // Add required entity
        Material material;
        if (TestUtil.findAll(em, Material.class).isEmpty()) {
            material = MaterialResourceIT.createUpdatedEntity(em);
            em.persist(material);
            em.flush();
        } else {
            material = TestUtil.findAll(em, Material.class).get(0);
        }
        materialRequest.setMaterial(material);
        return materialRequest;
    }

    @BeforeEach
    public void initTest() {
        materialRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterialRequest() throws Exception {
        int databaseSizeBeforeCreate = materialRequestRepository.findAll().size();
        // Create the MaterialRequest
        MaterialRequestDTO materialRequestDTO = materialRequestMapper.toDto(materialRequest);
        restMaterialRequestMockMvc.perform(post("/api/material-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the MaterialRequest in the database
        List<MaterialRequest> materialRequestList = materialRequestRepository.findAll();
        assertThat(materialRequestList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialRequest testMaterialRequest = materialRequestList.get(materialRequestList.size() - 1);
        assertThat(testMaterialRequest.getDateRequest()).isEqualTo(DEFAULT_DATE_REQUEST);
        assertThat(testMaterialRequest.isValidated()).isEqualTo(DEFAULT_VALIDATED);
        assertThat(testMaterialRequest.getQuantityRequested()).isEqualTo(DEFAULT_QUANTITY_REQUESTED);
    }

    @Test
    @Transactional
    public void createMaterialRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialRequestRepository.findAll().size();

        // Create the MaterialRequest with an existing ID
        materialRequest.setId(1L);
        MaterialRequestDTO materialRequestDTO = materialRequestMapper.toDto(materialRequest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialRequestMockMvc.perform(post("/api/material-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialRequest in the database
        List<MaterialRequest> materialRequestList = materialRequestRepository.findAll();
        assertThat(materialRequestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateRequestIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRequestRepository.findAll().size();
        // set the field null
        materialRequest.setDateRequest(null);

        // Create the MaterialRequest, which fails.
        MaterialRequestDTO materialRequestDTO = materialRequestMapper.toDto(materialRequest);


        restMaterialRequestMockMvc.perform(post("/api/material-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialRequestDTO)))
            .andExpect(status().isBadRequest());

        List<MaterialRequest> materialRequestList = materialRequestRepository.findAll();
        assertThat(materialRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityRequestedIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRequestRepository.findAll().size();
        // set the field null
        materialRequest.setQuantityRequested(null);

        // Create the MaterialRequest, which fails.
        MaterialRequestDTO materialRequestDTO = materialRequestMapper.toDto(materialRequest);


        restMaterialRequestMockMvc.perform(post("/api/material-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialRequestDTO)))
            .andExpect(status().isBadRequest());

        List<MaterialRequest> materialRequestList = materialRequestRepository.findAll();
        assertThat(materialRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaterialRequests() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList
        restMaterialRequestMockMvc.perform(get("/api/material-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateRequest").value(hasItem(DEFAULT_DATE_REQUEST.toString())))
            .andExpect(jsonPath("$.[*].validated").value(hasItem(DEFAULT_VALIDATED.booleanValue())))
            .andExpect(jsonPath("$.[*].quantityRequested").value(hasItem(DEFAULT_QUANTITY_REQUESTED)));
    }
    
    @Test
    @Transactional
    public void getMaterialRequest() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get the materialRequest
        restMaterialRequestMockMvc.perform(get("/api/material-requests/{id}", materialRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materialRequest.getId().intValue()))
            .andExpect(jsonPath("$.dateRequest").value(DEFAULT_DATE_REQUEST.toString()))
            .andExpect(jsonPath("$.validated").value(DEFAULT_VALIDATED.booleanValue()))
            .andExpect(jsonPath("$.quantityRequested").value(DEFAULT_QUANTITY_REQUESTED));
    }


    @Test
    @Transactional
    public void getMaterialRequestsByIdFiltering() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        Long id = materialRequest.getId();

        defaultMaterialRequestShouldBeFound("id.equals=" + id);
        defaultMaterialRequestShouldNotBeFound("id.notEquals=" + id);

        defaultMaterialRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMaterialRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultMaterialRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMaterialRequestShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMaterialRequestsByDateRequestIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where dateRequest equals to DEFAULT_DATE_REQUEST
        defaultMaterialRequestShouldBeFound("dateRequest.equals=" + DEFAULT_DATE_REQUEST);

        // Get all the materialRequestList where dateRequest equals to UPDATED_DATE_REQUEST
        defaultMaterialRequestShouldNotBeFound("dateRequest.equals=" + UPDATED_DATE_REQUEST);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByDateRequestIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where dateRequest not equals to DEFAULT_DATE_REQUEST
        defaultMaterialRequestShouldNotBeFound("dateRequest.notEquals=" + DEFAULT_DATE_REQUEST);

        // Get all the materialRequestList where dateRequest not equals to UPDATED_DATE_REQUEST
        defaultMaterialRequestShouldBeFound("dateRequest.notEquals=" + UPDATED_DATE_REQUEST);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByDateRequestIsInShouldWork() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where dateRequest in DEFAULT_DATE_REQUEST or UPDATED_DATE_REQUEST
        defaultMaterialRequestShouldBeFound("dateRequest.in=" + DEFAULT_DATE_REQUEST + "," + UPDATED_DATE_REQUEST);

        // Get all the materialRequestList where dateRequest equals to UPDATED_DATE_REQUEST
        defaultMaterialRequestShouldNotBeFound("dateRequest.in=" + UPDATED_DATE_REQUEST);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByDateRequestIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where dateRequest is not null
        defaultMaterialRequestShouldBeFound("dateRequest.specified=true");

        // Get all the materialRequestList where dateRequest is null
        defaultMaterialRequestShouldNotBeFound("dateRequest.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByValidatedIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where validated equals to DEFAULT_VALIDATED
        defaultMaterialRequestShouldBeFound("validated.equals=" + DEFAULT_VALIDATED);

        // Get all the materialRequestList where validated equals to UPDATED_VALIDATED
        defaultMaterialRequestShouldNotBeFound("validated.equals=" + UPDATED_VALIDATED);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByValidatedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where validated not equals to DEFAULT_VALIDATED
        defaultMaterialRequestShouldNotBeFound("validated.notEquals=" + DEFAULT_VALIDATED);

        // Get all the materialRequestList where validated not equals to UPDATED_VALIDATED
        defaultMaterialRequestShouldBeFound("validated.notEquals=" + UPDATED_VALIDATED);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByValidatedIsInShouldWork() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where validated in DEFAULT_VALIDATED or UPDATED_VALIDATED
        defaultMaterialRequestShouldBeFound("validated.in=" + DEFAULT_VALIDATED + "," + UPDATED_VALIDATED);

        // Get all the materialRequestList where validated equals to UPDATED_VALIDATED
        defaultMaterialRequestShouldNotBeFound("validated.in=" + UPDATED_VALIDATED);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByValidatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where validated is not null
        defaultMaterialRequestShouldBeFound("validated.specified=true");

        // Get all the materialRequestList where validated is null
        defaultMaterialRequestShouldNotBeFound("validated.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByQuantityRequestedIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where quantityRequested equals to DEFAULT_QUANTITY_REQUESTED
        defaultMaterialRequestShouldBeFound("quantityRequested.equals=" + DEFAULT_QUANTITY_REQUESTED);

        // Get all the materialRequestList where quantityRequested equals to UPDATED_QUANTITY_REQUESTED
        defaultMaterialRequestShouldNotBeFound("quantityRequested.equals=" + UPDATED_QUANTITY_REQUESTED);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByQuantityRequestedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where quantityRequested not equals to DEFAULT_QUANTITY_REQUESTED
        defaultMaterialRequestShouldNotBeFound("quantityRequested.notEquals=" + DEFAULT_QUANTITY_REQUESTED);

        // Get all the materialRequestList where quantityRequested not equals to UPDATED_QUANTITY_REQUESTED
        defaultMaterialRequestShouldBeFound("quantityRequested.notEquals=" + UPDATED_QUANTITY_REQUESTED);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByQuantityRequestedIsInShouldWork() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where quantityRequested in DEFAULT_QUANTITY_REQUESTED or UPDATED_QUANTITY_REQUESTED
        defaultMaterialRequestShouldBeFound("quantityRequested.in=" + DEFAULT_QUANTITY_REQUESTED + "," + UPDATED_QUANTITY_REQUESTED);

        // Get all the materialRequestList where quantityRequested equals to UPDATED_QUANTITY_REQUESTED
        defaultMaterialRequestShouldNotBeFound("quantityRequested.in=" + UPDATED_QUANTITY_REQUESTED);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByQuantityRequestedIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where quantityRequested is not null
        defaultMaterialRequestShouldBeFound("quantityRequested.specified=true");

        // Get all the materialRequestList where quantityRequested is null
        defaultMaterialRequestShouldNotBeFound("quantityRequested.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByQuantityRequestedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where quantityRequested is greater than or equal to DEFAULT_QUANTITY_REQUESTED
        defaultMaterialRequestShouldBeFound("quantityRequested.greaterThanOrEqual=" + DEFAULT_QUANTITY_REQUESTED);

        // Get all the materialRequestList where quantityRequested is greater than or equal to UPDATED_QUANTITY_REQUESTED
        defaultMaterialRequestShouldNotBeFound("quantityRequested.greaterThanOrEqual=" + UPDATED_QUANTITY_REQUESTED);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByQuantityRequestedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where quantityRequested is less than or equal to DEFAULT_QUANTITY_REQUESTED
        defaultMaterialRequestShouldBeFound("quantityRequested.lessThanOrEqual=" + DEFAULT_QUANTITY_REQUESTED);

        // Get all the materialRequestList where quantityRequested is less than or equal to SMALLER_QUANTITY_REQUESTED
        defaultMaterialRequestShouldNotBeFound("quantityRequested.lessThanOrEqual=" + SMALLER_QUANTITY_REQUESTED);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByQuantityRequestedIsLessThanSomething() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where quantityRequested is less than DEFAULT_QUANTITY_REQUESTED
        defaultMaterialRequestShouldNotBeFound("quantityRequested.lessThan=" + DEFAULT_QUANTITY_REQUESTED);

        // Get all the materialRequestList where quantityRequested is less than UPDATED_QUANTITY_REQUESTED
        defaultMaterialRequestShouldBeFound("quantityRequested.lessThan=" + UPDATED_QUANTITY_REQUESTED);
    }

    @Test
    @Transactional
    public void getAllMaterialRequestsByQuantityRequestedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        // Get all the materialRequestList where quantityRequested is greater than DEFAULT_QUANTITY_REQUESTED
        defaultMaterialRequestShouldNotBeFound("quantityRequested.greaterThan=" + DEFAULT_QUANTITY_REQUESTED);

        // Get all the materialRequestList where quantityRequested is greater than SMALLER_QUANTITY_REQUESTED
        defaultMaterialRequestShouldBeFound("quantityRequested.greaterThan=" + SMALLER_QUANTITY_REQUESTED);
    }


    @Test
    @Transactional
    public void getAllMaterialRequestsByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = materialRequest.getUser();
        materialRequestRepository.saveAndFlush(materialRequest);
        Long userId = user.getId();

        // Get all the materialRequestList where user equals to userId
        defaultMaterialRequestShouldBeFound("userId.equals=" + userId);

        // Get all the materialRequestList where user equals to userId + 1
        defaultMaterialRequestShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllMaterialRequestsByMaterialIsEqualToSomething() throws Exception {
        // Get already existing entity
        Material material = materialRequest.getMaterial();
        materialRequestRepository.saveAndFlush(materialRequest);
        Long materialId = material.getId();

        // Get all the materialRequestList where material equals to materialId
        defaultMaterialRequestShouldBeFound("materialId.equals=" + materialId);

        // Get all the materialRequestList where material equals to materialId + 1
        defaultMaterialRequestShouldNotBeFound("materialId.equals=" + (materialId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaterialRequestShouldBeFound(String filter) throws Exception {
        restMaterialRequestMockMvc.perform(get("/api/material-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateRequest").value(hasItem(DEFAULT_DATE_REQUEST.toString())))
            .andExpect(jsonPath("$.[*].validated").value(hasItem(DEFAULT_VALIDATED.booleanValue())))
            .andExpect(jsonPath("$.[*].quantityRequested").value(hasItem(DEFAULT_QUANTITY_REQUESTED)));

        // Check, that the count call also returns 1
        restMaterialRequestMockMvc.perform(get("/api/material-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaterialRequestShouldNotBeFound(String filter) throws Exception {
        restMaterialRequestMockMvc.perform(get("/api/material-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaterialRequestMockMvc.perform(get("/api/material-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMaterialRequest() throws Exception {
        // Get the materialRequest
        restMaterialRequestMockMvc.perform(get("/api/material-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterialRequest() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        int databaseSizeBeforeUpdate = materialRequestRepository.findAll().size();

        // Update the materialRequest
        MaterialRequest updatedMaterialRequest = materialRequestRepository.findById(materialRequest.getId()).get();
        // Disconnect from session so that the updates on updatedMaterialRequest are not directly saved in db
        em.detach(updatedMaterialRequest);
        updatedMaterialRequest
            .dateRequest(UPDATED_DATE_REQUEST)
            .validated(UPDATED_VALIDATED)
            .quantityRequested(UPDATED_QUANTITY_REQUESTED);
        MaterialRequestDTO materialRequestDTO = materialRequestMapper.toDto(updatedMaterialRequest);

        restMaterialRequestMockMvc.perform(put("/api/material-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialRequestDTO)))
            .andExpect(status().isOk());

        // Validate the MaterialRequest in the database
        List<MaterialRequest> materialRequestList = materialRequestRepository.findAll();
        assertThat(materialRequestList).hasSize(databaseSizeBeforeUpdate);
        MaterialRequest testMaterialRequest = materialRequestList.get(materialRequestList.size() - 1);
        assertThat(testMaterialRequest.getDateRequest()).isEqualTo(UPDATED_DATE_REQUEST);
        assertThat(testMaterialRequest.isValidated()).isEqualTo(UPDATED_VALIDATED);
        assertThat(testMaterialRequest.getQuantityRequested()).isEqualTo(UPDATED_QUANTITY_REQUESTED);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterialRequest() throws Exception {
        int databaseSizeBeforeUpdate = materialRequestRepository.findAll().size();

        // Create the MaterialRequest
        MaterialRequestDTO materialRequestDTO = materialRequestMapper.toDto(materialRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialRequestMockMvc.perform(put("/api/material-requests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialRequest in the database
        List<MaterialRequest> materialRequestList = materialRequestRepository.findAll();
        assertThat(materialRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMaterialRequest() throws Exception {
        // Initialize the database
        materialRequestRepository.saveAndFlush(materialRequest);

        int databaseSizeBeforeDelete = materialRequestRepository.findAll().size();

        // Delete the materialRequest
        restMaterialRequestMockMvc.perform(delete("/api/material-requests/{id}", materialRequest.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MaterialRequest> materialRequestList = materialRequestRepository.findAll();
        assertThat(materialRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

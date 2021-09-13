package fr.insy2s.web.rest;

import fr.insy2s.GdsiBackendApp;
import fr.insy2s.domain.MaterialType;
import fr.insy2s.repository.MaterialTypeRepository;
import fr.insy2s.service.MaterialTypeService;
import fr.insy2s.service.dto.MaterialTypeDTO;
import fr.insy2s.service.mapper.MaterialTypeMapper;

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
 * Integration tests for the {@link MaterialTypeResource} REST controller.
 */
@SpringBootTest(classes = GdsiBackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MaterialTypeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private MaterialTypeMapper materialTypeMapper;

    @Autowired
    private MaterialTypeService materialTypeService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialTypeMockMvc;

    private MaterialType materialType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialType createEntity(EntityManager em) {
        MaterialType materialType = new MaterialType()
            .code(DEFAULT_CODE)
            .label(DEFAULT_LABEL);
        return materialType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaterialType createUpdatedEntity(EntityManager em) {
        MaterialType materialType = new MaterialType()
            .code(UPDATED_CODE)
            .label(UPDATED_LABEL);
        return materialType;
    }

    @BeforeEach
    public void initTest() {
        materialType = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterialType() throws Exception {
        int databaseSizeBeforeCreate = materialTypeRepository.findAll().size();
        // Create the MaterialType
        MaterialTypeDTO materialTypeDTO = materialTypeMapper.toDto(materialType);
        restMaterialTypeMockMvc.perform(post("/api/material-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the MaterialType in the database
        List<MaterialType> materialTypeList = materialTypeRepository.findAll();
        assertThat(materialTypeList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialType testMaterialType = materialTypeList.get(materialTypeList.size() - 1);
        assertThat(testMaterialType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testMaterialType.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createMaterialTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialTypeRepository.findAll().size();

        // Create the MaterialType with an existing ID
        materialType.setId(1L);
        MaterialTypeDTO materialTypeDTO = materialTypeMapper.toDto(materialType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialTypeMockMvc.perform(post("/api/material-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialType in the database
        List<MaterialType> materialTypeList = materialTypeRepository.findAll();
        assertThat(materialTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialTypeRepository.findAll().size();
        // set the field null
        materialType.setCode(null);

        // Create the MaterialType, which fails.
        MaterialTypeDTO materialTypeDTO = materialTypeMapper.toDto(materialType);


        restMaterialTypeMockMvc.perform(post("/api/material-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialTypeDTO)))
            .andExpect(status().isBadRequest());

        List<MaterialType> materialTypeList = materialTypeRepository.findAll();
        assertThat(materialTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialTypeRepository.findAll().size();
        // set the field null
        materialType.setLabel(null);

        // Create the MaterialType, which fails.
        MaterialTypeDTO materialTypeDTO = materialTypeMapper.toDto(materialType);


        restMaterialTypeMockMvc.perform(post("/api/material-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialTypeDTO)))
            .andExpect(status().isBadRequest());

        List<MaterialType> materialTypeList = materialTypeRepository.findAll();
        assertThat(materialTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaterialTypes() throws Exception {
        // Initialize the database
        materialTypeRepository.saveAndFlush(materialType);

        // Get all the materialTypeList
        restMaterialTypeMockMvc.perform(get("/api/material-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialType.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }
    
    @Test
    @Transactional
    public void getMaterialType() throws Exception {
        // Initialize the database
        materialTypeRepository.saveAndFlush(materialType);

        // Get the materialType
        restMaterialTypeMockMvc.perform(get("/api/material-types/{id}", materialType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(materialType.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }
    @Test
    @Transactional
    public void getNonExistingMaterialType() throws Exception {
        // Get the materialType
        restMaterialTypeMockMvc.perform(get("/api/material-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterialType() throws Exception {
        // Initialize the database
        materialTypeRepository.saveAndFlush(materialType);

        int databaseSizeBeforeUpdate = materialTypeRepository.findAll().size();

        // Update the materialType
        MaterialType updatedMaterialType = materialTypeRepository.findById(materialType.getId()).get();
        // Disconnect from session so that the updates on updatedMaterialType are not directly saved in db
        em.detach(updatedMaterialType);
        updatedMaterialType
            .code(UPDATED_CODE)
            .label(UPDATED_LABEL);
        MaterialTypeDTO materialTypeDTO = materialTypeMapper.toDto(updatedMaterialType);

        restMaterialTypeMockMvc.perform(put("/api/material-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialTypeDTO)))
            .andExpect(status().isOk());

        // Validate the MaterialType in the database
        List<MaterialType> materialTypeList = materialTypeRepository.findAll();
        assertThat(materialTypeList).hasSize(databaseSizeBeforeUpdate);
        MaterialType testMaterialType = materialTypeList.get(materialTypeList.size() - 1);
        assertThat(testMaterialType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testMaterialType.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterialType() throws Exception {
        int databaseSizeBeforeUpdate = materialTypeRepository.findAll().size();

        // Create the MaterialType
        MaterialTypeDTO materialTypeDTO = materialTypeMapper.toDto(materialType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialTypeMockMvc.perform(put("/api/material-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialType in the database
        List<MaterialType> materialTypeList = materialTypeRepository.findAll();
        assertThat(materialTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMaterialType() throws Exception {
        // Initialize the database
        materialTypeRepository.saveAndFlush(materialType);

        int databaseSizeBeforeDelete = materialTypeRepository.findAll().size();

        // Delete the materialType
        restMaterialTypeMockMvc.perform(delete("/api/material-types/{id}", materialType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MaterialType> materialTypeList = materialTypeRepository.findAll();
        assertThat(materialTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

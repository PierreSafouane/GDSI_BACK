package fr.insy2s.web.rest;

import fr.insy2s.GdsiBackendApp;
import fr.insy2s.domain.Material;
import fr.insy2s.domain.MaterialType;
import fr.insy2s.domain.Room;
import fr.insy2s.repository.MaterialRepository;
import fr.insy2s.service.MaterialService;
import fr.insy2s.service.dto.MaterialDTO;
import fr.insy2s.service.mapper.MaterialMapper;
import fr.insy2s.service.dto.MaterialCriteria;
import fr.insy2s.service.MaterialQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MaterialResource} REST controller.
 */
@SpringBootTest(classes = GdsiBackendApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class MaterialResourceIT {

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_PRICE = 0D;
    private static final Double UPDATED_PRICE = 1D;
    private static final Double SMALLER_PRICE = 0D - 1D;

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    @Autowired
    private MaterialRepository materialRepository;

    @Mock
    private MaterialRepository materialRepositoryMock;

    @Autowired
    private MaterialMapper materialMapper;

    @Mock
    private MaterialService materialServiceMock;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialQueryService materialQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaterialMockMvc;

    private Material material;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createEntity(EntityManager em) {
        Material material = new Material()
            .imageUrl(DEFAULT_IMAGE_URL)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .price(DEFAULT_PRICE)
            .quantity(DEFAULT_QUANTITY)
            .link(DEFAULT_LINK);
        return material;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Material createUpdatedEntity(EntityManager em) {
        Material material = new Material()
            .imageUrl(UPDATED_IMAGE_URL)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .link(UPDATED_LINK);
        return material;
    }

    @BeforeEach
    public void initTest() {
        material = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterial() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().size();
        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);
        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isCreated());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate + 1);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testMaterial.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMaterial.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMaterial.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testMaterial.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testMaterial.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void createMaterialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialRepository.findAll().size();

        // Create the Material with an existing ID
        material.setId(1L);
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setName(null);

        // Create the Material, which fails.
        MaterialDTO materialDTO = materialMapper.toDto(material);


        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = materialRepository.findAll().size();
        // set the field null
        material.setQuantity(null);

        // Create the Material, which fails.
        MaterialDTO materialDTO = materialMapper.toDto(material);


        restMaterialMockMvc.perform(post("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMaterials() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList
        restMaterialMockMvc.perform(get("/api/materials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllMaterialsWithEagerRelationshipsIsEnabled() throws Exception {
        when(materialServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMaterialMockMvc.perform(get("/api/materials?eagerload=true"))
            .andExpect(status().isOk());

        verify(materialServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllMaterialsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(materialServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMaterialMockMvc.perform(get("/api/materials?eagerload=true"))
            .andExpect(status().isOk());

        verify(materialServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get the material
        restMaterialMockMvc.perform(get("/api/materials/{id}", material.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(material.getId().intValue()))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK));
    }


    @Test
    @Transactional
    public void getMaterialsByIdFiltering() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        Long id = material.getId();

        defaultMaterialShouldBeFound("id.equals=" + id);
        defaultMaterialShouldNotBeFound("id.notEquals=" + id);

        defaultMaterialShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMaterialShouldNotBeFound("id.greaterThan=" + id);

        defaultMaterialShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMaterialShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMaterialsByImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where imageUrl equals to DEFAULT_IMAGE_URL
        defaultMaterialShouldBeFound("imageUrl.equals=" + DEFAULT_IMAGE_URL);

        // Get all the materialList where imageUrl equals to UPDATED_IMAGE_URL
        defaultMaterialShouldNotBeFound("imageUrl.equals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllMaterialsByImageUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where imageUrl not equals to DEFAULT_IMAGE_URL
        defaultMaterialShouldNotBeFound("imageUrl.notEquals=" + DEFAULT_IMAGE_URL);

        // Get all the materialList where imageUrl not equals to UPDATED_IMAGE_URL
        defaultMaterialShouldBeFound("imageUrl.notEquals=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllMaterialsByImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where imageUrl in DEFAULT_IMAGE_URL or UPDATED_IMAGE_URL
        defaultMaterialShouldBeFound("imageUrl.in=" + DEFAULT_IMAGE_URL + "," + UPDATED_IMAGE_URL);

        // Get all the materialList where imageUrl equals to UPDATED_IMAGE_URL
        defaultMaterialShouldNotBeFound("imageUrl.in=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllMaterialsByImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where imageUrl is not null
        defaultMaterialShouldBeFound("imageUrl.specified=true");

        // Get all the materialList where imageUrl is null
        defaultMaterialShouldNotBeFound("imageUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaterialsByImageUrlContainsSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where imageUrl contains DEFAULT_IMAGE_URL
        defaultMaterialShouldBeFound("imageUrl.contains=" + DEFAULT_IMAGE_URL);

        // Get all the materialList where imageUrl contains UPDATED_IMAGE_URL
        defaultMaterialShouldNotBeFound("imageUrl.contains=" + UPDATED_IMAGE_URL);
    }

    @Test
    @Transactional
    public void getAllMaterialsByImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where imageUrl does not contain DEFAULT_IMAGE_URL
        defaultMaterialShouldNotBeFound("imageUrl.doesNotContain=" + DEFAULT_IMAGE_URL);

        // Get all the materialList where imageUrl does not contain UPDATED_IMAGE_URL
        defaultMaterialShouldBeFound("imageUrl.doesNotContain=" + UPDATED_IMAGE_URL);
    }


    @Test
    @Transactional
    public void getAllMaterialsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name equals to DEFAULT_NAME
        defaultMaterialShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the materialList where name equals to UPDATED_NAME
        defaultMaterialShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMaterialsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name not equals to DEFAULT_NAME
        defaultMaterialShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the materialList where name not equals to UPDATED_NAME
        defaultMaterialShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMaterialsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMaterialShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the materialList where name equals to UPDATED_NAME
        defaultMaterialShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMaterialsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name is not null
        defaultMaterialShouldBeFound("name.specified=true");

        // Get all the materialList where name is null
        defaultMaterialShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaterialsByNameContainsSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name contains DEFAULT_NAME
        defaultMaterialShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the materialList where name contains UPDATED_NAME
        defaultMaterialShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMaterialsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where name does not contain DEFAULT_NAME
        defaultMaterialShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the materialList where name does not contain UPDATED_NAME
        defaultMaterialShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllMaterialsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where description equals to DEFAULT_DESCRIPTION
        defaultMaterialShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the materialList where description equals to UPDATED_DESCRIPTION
        defaultMaterialShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMaterialsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where description not equals to DEFAULT_DESCRIPTION
        defaultMaterialShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the materialList where description not equals to UPDATED_DESCRIPTION
        defaultMaterialShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMaterialsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMaterialShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the materialList where description equals to UPDATED_DESCRIPTION
        defaultMaterialShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMaterialsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where description is not null
        defaultMaterialShouldBeFound("description.specified=true");

        // Get all the materialList where description is null
        defaultMaterialShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaterialsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where description contains DEFAULT_DESCRIPTION
        defaultMaterialShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the materialList where description contains UPDATED_DESCRIPTION
        defaultMaterialShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllMaterialsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where description does not contain DEFAULT_DESCRIPTION
        defaultMaterialShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the materialList where description does not contain UPDATED_DESCRIPTION
        defaultMaterialShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllMaterialsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where price equals to DEFAULT_PRICE
        defaultMaterialShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the materialList where price equals to UPDATED_PRICE
        defaultMaterialShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllMaterialsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where price not equals to DEFAULT_PRICE
        defaultMaterialShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the materialList where price not equals to UPDATED_PRICE
        defaultMaterialShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllMaterialsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultMaterialShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the materialList where price equals to UPDATED_PRICE
        defaultMaterialShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllMaterialsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where price is not null
        defaultMaterialShouldBeFound("price.specified=true");

        // Get all the materialList where price is null
        defaultMaterialShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaterialsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where price is greater than or equal to DEFAULT_PRICE
        defaultMaterialShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the materialList where price is greater than or equal to UPDATED_PRICE
        defaultMaterialShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllMaterialsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where price is less than or equal to DEFAULT_PRICE
        defaultMaterialShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the materialList where price is less than or equal to SMALLER_PRICE
        defaultMaterialShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllMaterialsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where price is less than DEFAULT_PRICE
        defaultMaterialShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the materialList where price is less than UPDATED_PRICE
        defaultMaterialShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllMaterialsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where price is greater than DEFAULT_PRICE
        defaultMaterialShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the materialList where price is greater than SMALLER_PRICE
        defaultMaterialShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllMaterialsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where quantity equals to DEFAULT_QUANTITY
        defaultMaterialShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the materialList where quantity equals to UPDATED_QUANTITY
        defaultMaterialShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllMaterialsByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where quantity not equals to DEFAULT_QUANTITY
        defaultMaterialShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the materialList where quantity not equals to UPDATED_QUANTITY
        defaultMaterialShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllMaterialsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultMaterialShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the materialList where quantity equals to UPDATED_QUANTITY
        defaultMaterialShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllMaterialsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where quantity is not null
        defaultMaterialShouldBeFound("quantity.specified=true");

        // Get all the materialList where quantity is null
        defaultMaterialShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaterialsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultMaterialShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the materialList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultMaterialShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllMaterialsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultMaterialShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the materialList where quantity is less than or equal to SMALLER_QUANTITY
        defaultMaterialShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllMaterialsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where quantity is less than DEFAULT_QUANTITY
        defaultMaterialShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the materialList where quantity is less than UPDATED_QUANTITY
        defaultMaterialShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllMaterialsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where quantity is greater than DEFAULT_QUANTITY
        defaultMaterialShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the materialList where quantity is greater than SMALLER_QUANTITY
        defaultMaterialShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllMaterialsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where link equals to DEFAULT_LINK
        defaultMaterialShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the materialList where link equals to UPDATED_LINK
        defaultMaterialShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllMaterialsByLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where link not equals to DEFAULT_LINK
        defaultMaterialShouldNotBeFound("link.notEquals=" + DEFAULT_LINK);

        // Get all the materialList where link not equals to UPDATED_LINK
        defaultMaterialShouldBeFound("link.notEquals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllMaterialsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where link in DEFAULT_LINK or UPDATED_LINK
        defaultMaterialShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the materialList where link equals to UPDATED_LINK
        defaultMaterialShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllMaterialsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where link is not null
        defaultMaterialShouldBeFound("link.specified=true");

        // Get all the materialList where link is null
        defaultMaterialShouldNotBeFound("link.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaterialsByLinkContainsSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where link contains DEFAULT_LINK
        defaultMaterialShouldBeFound("link.contains=" + DEFAULT_LINK);

        // Get all the materialList where link contains UPDATED_LINK
        defaultMaterialShouldNotBeFound("link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllMaterialsByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        // Get all the materialList where link does not contain DEFAULT_LINK
        defaultMaterialShouldNotBeFound("link.doesNotContain=" + DEFAULT_LINK);

        // Get all the materialList where link does not contain UPDATED_LINK
        defaultMaterialShouldBeFound("link.doesNotContain=" + UPDATED_LINK);
    }


    @Test
    @Transactional
    public void getAllMaterialsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);
        MaterialType type = MaterialTypeResourceIT.createEntity(em);
        em.persist(type);
        em.flush();
        material.setType(type);
        materialRepository.saveAndFlush(material);
        Long typeId = type.getId();

        // Get all the materialList where type equals to typeId
        defaultMaterialShouldBeFound("typeId.equals=" + typeId);

        // Get all the materialList where type equals to typeId + 1
        defaultMaterialShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }


    @Test
    @Transactional
    public void getAllMaterialsByRoomsIsEqualToSomething() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);
        Room rooms = RoomResourceIT.createEntity(em);
        em.persist(rooms);
        em.flush();
        material.addRooms(rooms);
        materialRepository.saveAndFlush(material);
        Long roomsId = rooms.getId();

        // Get all the materialList where rooms equals to roomsId
        defaultMaterialShouldBeFound("roomsId.equals=" + roomsId);

        // Get all the materialList where rooms equals to roomsId + 1
        defaultMaterialShouldNotBeFound("roomsId.equals=" + (roomsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaterialShouldBeFound(String filter) throws Exception {
        restMaterialMockMvc.perform(get("/api/materials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(material.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)));

        // Check, that the count call also returns 1
        restMaterialMockMvc.perform(get("/api/materials/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaterialShouldNotBeFound(String filter) throws Exception {
        restMaterialMockMvc.perform(get("/api/materials?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaterialMockMvc.perform(get("/api/materials/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMaterial() throws Exception {
        // Get the material
        restMaterialMockMvc.perform(get("/api/materials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Update the material
        Material updatedMaterial = materialRepository.findById(material.getId()).get();
        // Disconnect from session so that the updates on updatedMaterial are not directly saved in db
        em.detach(updatedMaterial);
        updatedMaterial
            .imageUrl(UPDATED_IMAGE_URL)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .quantity(UPDATED_QUANTITY)
            .link(UPDATED_LINK);
        MaterialDTO materialDTO = materialMapper.toDto(updatedMaterial);

        restMaterialMockMvc.perform(put("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isOk());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
        Material testMaterial = materialList.get(materialList.size() - 1);
        assertThat(testMaterial.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testMaterial.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMaterial.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMaterial.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testMaterial.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testMaterial.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterial() throws Exception {
        int databaseSizeBeforeUpdate = materialRepository.findAll().size();

        // Create the Material
        MaterialDTO materialDTO = materialMapper.toDto(material);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialMockMvc.perform(put("/api/materials")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(materialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Material in the database
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMaterial() throws Exception {
        // Initialize the database
        materialRepository.saveAndFlush(material);

        int databaseSizeBeforeDelete = materialRepository.findAll().size();

        // Delete the material
        restMaterialMockMvc.perform(delete("/api/materials/{id}", material.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Material> materialList = materialRepository.findAll();
        assertThat(materialList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package fr.insy2s.web.rest;

import fr.insy2s.service.MaterialTypeService;
import fr.insy2s.web.rest.errors.BadRequestAlertException;
import fr.insy2s.service.dto.MaterialTypeDTO;

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
 * REST controller for managing {@link fr.insy2s.domain.MaterialType}.
 */
@RestController
@RequestMapping("/api")
public class MaterialTypeResource {

    private final Logger log = LoggerFactory.getLogger(MaterialTypeResource.class);

    private static final String ENTITY_NAME = "materialType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MaterialTypeService materialTypeService;

    public MaterialTypeResource(MaterialTypeService materialTypeService) {
        this.materialTypeService = materialTypeService;
    }

    /**
     * {@code POST  /material-types} : Create a new materialType.
     *
     * @param materialTypeDTO the materialTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new materialTypeDTO, or with status {@code 400 (Bad Request)} if the materialType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/material-types")
    public ResponseEntity<MaterialTypeDTO> createMaterialType(@Valid @RequestBody MaterialTypeDTO materialTypeDTO) throws URISyntaxException {
        log.debug("REST request to save MaterialType : {}", materialTypeDTO);
        if (materialTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new materialType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MaterialTypeDTO result = materialTypeService.save(materialTypeDTO);
        return ResponseEntity.created(new URI("/api/material-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /material-types} : Updates an existing materialType.
     *
     * @param materialTypeDTO the materialTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated materialTypeDTO,
     * or with status {@code 400 (Bad Request)} if the materialTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the materialTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/material-types")
    public ResponseEntity<MaterialTypeDTO> updateMaterialType(@Valid @RequestBody MaterialTypeDTO materialTypeDTO) throws URISyntaxException {
        log.debug("REST request to update MaterialType : {}", materialTypeDTO);
        if (materialTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MaterialTypeDTO result = materialTypeService.save(materialTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, materialTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /material-types} : get all the materialTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of materialTypes in body.
     */
    @GetMapping("/material-types")
    public List<MaterialTypeDTO> getAllMaterialTypes() {
        log.debug("REST request to get all MaterialTypes");
        return materialTypeService.findAll();
    }

    /**
     * {@code GET  /material-types/:id} : get the "id" materialType.
     *
     * @param id the id of the materialTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the materialTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/material-types/{id}")
    public ResponseEntity<MaterialTypeDTO> getMaterialType(@PathVariable Long id) {
        log.debug("REST request to get MaterialType : {}", id);
        Optional<MaterialTypeDTO> materialTypeDTO = materialTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(materialTypeDTO);
    }

    /**
     * {@code DELETE  /material-types/:id} : delete the "id" materialType.
     *
     * @param id the id of the materialTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/material-types/{id}")
    public ResponseEntity<Void> deleteMaterialType(@PathVariable Long id) {
        log.debug("REST request to delete MaterialType : {}", id);
        materialTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

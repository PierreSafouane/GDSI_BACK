package fr.insy2s.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import fr.insy2s.domain.MaterialRequest;
import fr.insy2s.domain.*; // for static metamodels
import fr.insy2s.repository.MaterialRequestRepository;
import fr.insy2s.service.dto.MaterialRequestCriteria;
import fr.insy2s.service.dto.MaterialRequestDTO;
import fr.insy2s.service.mapper.MaterialRequestMapper;

/**
 * Service for executing complex queries for {@link MaterialRequest} entities in the database.
 * The main input is a {@link MaterialRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MaterialRequestDTO} or a {@link Page} of {@link MaterialRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaterialRequestQueryService extends QueryService<MaterialRequest> {

    private final Logger log = LoggerFactory.getLogger(MaterialRequestQueryService.class);

    private final MaterialRequestRepository materialRequestRepository;

    private final MaterialRequestMapper materialRequestMapper;

    public MaterialRequestQueryService(MaterialRequestRepository materialRequestRepository, MaterialRequestMapper materialRequestMapper) {
        this.materialRequestRepository = materialRequestRepository;
        this.materialRequestMapper = materialRequestMapper;
    }

    /**
     * Return a {@link List} of {@link MaterialRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MaterialRequestDTO> findByCriteria(MaterialRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MaterialRequest> specification = createSpecification(criteria);
        return materialRequestMapper.toDto(materialRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MaterialRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaterialRequestDTO> findByCriteria(MaterialRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MaterialRequest> specification = createSpecification(criteria);
        return materialRequestRepository.findAll(specification, page)
            .map(materialRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaterialRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MaterialRequest> specification = createSpecification(criteria);
        return materialRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link MaterialRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MaterialRequest> createSpecification(MaterialRequestCriteria criteria) {
        Specification<MaterialRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MaterialRequest_.id));
            }
            if (criteria.getDateRequest() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateRequest(), MaterialRequest_.dateRequest));
            }
            if (criteria.getValidated() != null) {
                specification = specification.and(buildSpecification(criteria.getValidated(), MaterialRequest_.validated));
            }
            if (criteria.getQuantityRequested() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantityRequested(), MaterialRequest_.quantityRequested));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(MaterialRequest_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getMaterialId() != null) {
                specification = specification.and(buildSpecification(criteria.getMaterialId(),
                    root -> root.join(MaterialRequest_.material, JoinType.LEFT).get(Material_.id)));
            }
        }
        return specification;
    }
}

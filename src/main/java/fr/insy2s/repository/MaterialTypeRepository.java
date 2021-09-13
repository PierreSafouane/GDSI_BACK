package fr.insy2s.repository;

import fr.insy2s.domain.MaterialType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MaterialType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialTypeRepository extends JpaRepository<MaterialType, Long> {
}

package fr.insy2s.repository;

import fr.insy2s.domain.MaterialRequest;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the MaterialRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, Long>, JpaSpecificationExecutor<MaterialRequest> {

    @Query("select materialRequest from MaterialRequest materialRequest where materialRequest.user.login = ?#{principal.username}")
    List<MaterialRequest> findByUserIsCurrentUser();
}

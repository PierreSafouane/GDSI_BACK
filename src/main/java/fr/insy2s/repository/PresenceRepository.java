package fr.insy2s.repository;

import fr.insy2s.domain.Presence;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Presence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresenceRepository extends JpaRepository<Presence, Long> {

    @Query("select presence from Presence presence where presence.appUser.login = ?#{principal.username}")
    List<Presence> findByAppUserIsCurrentUser();
}

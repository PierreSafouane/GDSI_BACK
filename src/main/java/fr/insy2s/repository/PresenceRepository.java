package fr.insy2s.repository;

import fr.insy2s.domain.Booking;
import fr.insy2s.domain.Presence;

import fr.insy2s.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data  repository for the Presence entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresenceRepository extends JpaRepository<Presence, Long> {

    @Query("select presence from Presence presence where presence.appUser.login = ?#{principal.username}")
    List<Presence> findByAppUserIsCurrentUser();

    List<Presence> findByBookingId(Long id);

    List<Presence> findByAppUserId(Long id);

    List<Presence> findByAppUserIdAndBookingStartAtBefore(Long id, Instant startDate);
    List<Presence> findByAppUserIdAndBookingFinishAtAfter(Long id, Instant startDate);
}

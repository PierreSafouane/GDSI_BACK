package fr.insy2s.repository;

import fr.insy2s.domain.RoomImage;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RoomImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
}

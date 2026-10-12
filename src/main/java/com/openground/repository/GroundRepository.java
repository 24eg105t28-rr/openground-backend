package com.openground.repository;

import com.openground.entity.Ground;
import com.openground.entity.GroundStatus;
import com.openground.entity.PitchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroundRepository extends JpaRepository<Ground, UUID> {
    Optional<Ground> findBySlug(String slug);
    List<Ground> findByStatus(GroundStatus status);
    List<Ground> findByOwnerId(UUID ownerId);

    @Query("SELECT g FROM Ground g WHERE g.status = 'APPROVED' " +
           "AND (:city IS NULL OR LOWER(g.city) = LOWER(:city)) " +
           "AND (:pitchType IS NULL OR g.pitchType = :pitchType) " +
           "AND (:floodlights IS NULL OR g.floodlights = :floodlights) " +
           "AND (:maxHourlyRate IS NULL OR g.hourlyBaseRate <= :maxHourlyRate) " +
           "AND (:search IS NULL OR LOWER(g.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "     OR LOWER(g.address) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Ground> searchGrounds(@Param("city") String city,
                               @Param("pitchType") PitchType pitchType,
                               @Param("floodlights") Boolean floodlights,
                               @Param("maxHourlyRate") BigDecimal maxHourlyRate,
                               @Param("search") String search);
}

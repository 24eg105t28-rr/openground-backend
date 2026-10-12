package com.openground.repository;

import com.openground.entity.AvailabilityClosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AvailabilityClosureRepository extends JpaRepository<AvailabilityClosure, UUID> {
    List<AvailabilityClosure> findByGroundId(UUID groundId);

    @Query("SELECT c FROM AvailabilityClosure c WHERE c.ground.id = :groundId " +
           "AND :date BETWEEN c.startDate AND c.endDate")
    List<AvailabilityClosure> findClosuresOnDate(@Param("groundId") UUID groundId, @Param("date") LocalDate date);
}

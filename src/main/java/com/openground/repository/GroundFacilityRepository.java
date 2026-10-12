package com.openground.repository;

import com.openground.entity.GroundFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroundFacilityRepository extends JpaRepository<GroundFacility, UUID> {
    List<GroundFacility> findByGroundId(UUID groundId);
}

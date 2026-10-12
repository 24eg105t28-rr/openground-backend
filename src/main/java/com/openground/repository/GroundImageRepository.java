package com.openground.repository;

import com.openground.entity.GroundImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroundImageRepository extends JpaRepository<GroundImage, UUID> {
    List<GroundImage> findByGroundId(UUID groundId);
}

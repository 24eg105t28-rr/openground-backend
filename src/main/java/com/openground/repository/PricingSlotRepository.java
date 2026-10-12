package com.openground.repository;

import com.openground.entity.PricingSlot;
import com.openground.entity.SlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PricingSlotRepository extends JpaRepository<PricingSlot, UUID> {
    List<PricingSlot> findByGroundId(UUID groundId);
    Optional<PricingSlot> findByGroundIdAndSlotType(UUID groundId, SlotType slotType);
}

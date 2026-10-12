package com.openground.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "ground_facilities")
public class GroundFacility {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ground_id")
    private Ground ground;

    @Column(name = "facility_name", nullable = false)
    private String facilityName;

    public GroundFacility() {
    }

    public GroundFacility(UUID id, Ground ground, String facilityName) {
        this.id = id != null ? id : UUID.randomUUID();
        this.ground = ground;
        this.facilityName = facilityName;
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Ground getGround() { return ground; }
    public void setGround(Ground ground) { this.ground = ground; }

    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String facilityName) { this.facilityName = facilityName; }
}

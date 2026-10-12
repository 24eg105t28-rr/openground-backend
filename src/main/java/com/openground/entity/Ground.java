package com.openground.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "grounds")
public class Ground {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(name = "boundary_radius_meters", nullable = false)
    private Integer boundaryRadiusMeters;

    @Enumerated(EnumType.STRING)
    @Column(name = "pitch_type", nullable = false)
    private PitchType pitchType;

    @Column(nullable = false)
    private Boolean floodlights = false;

    @Column(name = "pavilion_details", columnDefinition = "TEXT")
    private String pavilionDetails;

    @Column(name = "seating_capacity", nullable = false)
    private Integer seatingCapacity = 0;

    @Column(name = "hourly_base_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyBaseRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GroundStatus status = GroundStatus.APPROVED;

    @OneToMany(mappedBy = "ground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroundImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "ground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroundFacility> facilities = new ArrayList<>();

    @OneToMany(mappedBy = "ground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PricingSlot> slots = new ArrayList<>();

    @OneToMany(mappedBy = "ground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AvailabilityClosure> closures = new ArrayList<>();

    @OneToMany(mappedBy = "ground", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Ground() {
    }

    public Ground(UUID id, User owner, String name, String slug, String description,
                  String address, String city, String state, Integer boundaryRadiusMeters,
                  PitchType pitchType, Boolean floodlights, String pavilionDetails,
                  Integer seatingCapacity, BigDecimal hourlyBaseRate, GroundStatus status) {
        this.id = id != null ? id : UUID.randomUUID();
        this.owner = owner;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.address = address;
        this.city = city;
        this.state = state;
        this.boundaryRadiusMeters = boundaryRadiusMeters;
        this.pitchType = pitchType;
        this.floodlights = floodlights != null ? floodlights : false;
        this.pavilionDetails = pavilionDetails;
        this.seatingCapacity = seatingCapacity != null ? seatingCapacity : 0;
        this.hourlyBaseRate = hourlyBaseRate;
        this.status = status != null ? status : GroundStatus.APPROVED;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public Integer getBoundaryRadiusMeters() { return boundaryRadiusMeters; }
    public void setBoundaryRadiusMeters(Integer boundaryRadiusMeters) { this.boundaryRadiusMeters = boundaryRadiusMeters; }

    public PitchType getPitchType() { return pitchType; }
    public void setPitchType(PitchType pitchType) { this.pitchType = pitchType; }

    public Boolean getFloodlights() { return floodlights; }
    public void setFloodlights(Boolean floodlights) { this.floodlights = floodlights; }

    public String getPavilionDetails() { return pavilionDetails; }
    public void setPavilionDetails(String pavilionDetails) { this.pavilionDetails = pavilionDetails; }

    public Integer getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public BigDecimal getHourlyBaseRate() { return hourlyBaseRate; }
    public void setHourlyBaseRate(BigDecimal hourlyBaseRate) { this.hourlyBaseRate = hourlyBaseRate; }

    public GroundStatus getStatus() { return status; }
    public void setStatus(GroundStatus status) { this.status = status; }

    public List<GroundImage> getImages() { return images; }
    public void setImages(List<GroundImage> images) { this.images = images; }

    public List<GroundFacility> getFacilities() { return facilities; }
    public void setFacilities(List<GroundFacility> facilities) { this.facilities = facilities; }

    public List<PricingSlot> getSlots() { return slots; }
    public void setSlots(List<PricingSlot> slots) { this.slots = slots; }

    public List<AvailabilityClosure> getClosures() { return closures; }
    public void setClosures(List<AvailabilityClosure> closures) { this.closures = closures; }

    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

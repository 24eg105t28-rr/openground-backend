package com.openground.dto;

import com.openground.entity.GroundStatus;
import com.openground.entity.PitchType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GroundDetailDto {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String address;
    private String city;
    private String state;
    private Integer boundaryRadiusMeters;
    private PitchType pitchType;
    private boolean floodlights;
    private String pavilionDetails;
    private Integer seatingCapacity;
    private BigDecimal hourlyBaseRate;
    private GroundStatus status;
    private double averageRating;
    private int reviewCount;
    private List<GroundImageDto> images;
    private List<String> facilities;
    private List<PricingSlotDto> slots;
    private List<ReviewDto> reviews;
    private LocalDateTime createdAt;

    public GroundDetailDto() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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

    public boolean isFloodlights() { return floodlights; }
    public void setFloodlights(boolean floodlights) { this.floodlights = floodlights; }

    public String getPavilionDetails() { return pavilionDetails; }
    public void setPavilionDetails(String pavilionDetails) { this.pavilionDetails = pavilionDetails; }

    public Integer getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public BigDecimal getHourlyBaseRate() { return hourlyBaseRate; }
    public void setHourlyBaseRate(BigDecimal hourlyBaseRate) { this.hourlyBaseRate = hourlyBaseRate; }

    public GroundStatus getStatus() { return status; }
    public void setStatus(GroundStatus status) { this.status = status; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public List<GroundImageDto> getImages() { return images; }
    public void setImages(List<GroundImageDto> images) { this.images = images; }

    public List<String> getFacilities() { return facilities; }
    public void setFacilities(List<String> facilities) { this.facilities = facilities; }

    public List<PricingSlotDto> getSlots() { return slots; }
    public void setSlots(List<PricingSlotDto> slots) { this.slots = slots; }

    public List<ReviewDto> getReviews() { return reviews; }
    public void setReviews(List<ReviewDto> reviews) { this.reviews = reviews; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

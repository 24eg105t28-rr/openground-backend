package com.openground.dto;

import com.openground.entity.PitchType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class GroundSummaryDto {
    private UUID id;
    private String name;
    private String slug;
    private String city;
    private String state;
    private String address;
    private Integer boundaryRadiusMeters;
    private PitchType pitchType;
    private boolean floodlights;
    private Integer seatingCapacity;
    private BigDecimal hourlyBaseRate;
    private String primaryImageUrl;
    private double averageRating;
    private int reviewCount;
    private List<String> keyFacilities;

    public GroundSummaryDto() {
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getBoundaryRadiusMeters() { return boundaryRadiusMeters; }
    public void setBoundaryRadiusMeters(Integer boundaryRadiusMeters) { this.boundaryRadiusMeters = boundaryRadiusMeters; }

    public PitchType getPitchType() { return pitchType; }
    public void setPitchType(PitchType pitchType) { this.pitchType = pitchType; }

    public boolean isFloodlights() { return floodlights; }
    public void setFloodlights(boolean floodlights) { this.floodlights = floodlights; }

    public Integer getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(Integer seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public BigDecimal getHourlyBaseRate() { return hourlyBaseRate; }
    public void setHourlyBaseRate(BigDecimal hourlyBaseRate) { this.hourlyBaseRate = hourlyBaseRate; }

    public String getPrimaryImageUrl() { return primaryImageUrl; }
    public void setPrimaryImageUrl(String primaryImageUrl) { this.primaryImageUrl = primaryImageUrl; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public List<String> getKeyFacilities() { return keyFacilities; }
    public void setKeyFacilities(List<String> keyFacilities) { this.keyFacilities = keyFacilities; }
}

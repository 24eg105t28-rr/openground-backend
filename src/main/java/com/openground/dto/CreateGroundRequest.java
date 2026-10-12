package com.openground.dto;

import com.openground.entity.PitchType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class CreateGroundRequest {

    @NotBlank(message = "Ground name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotNull(message = "Boundary radius is required")
    @Min(value = 55, message = "Outdoor cricket grounds must have at least 55m boundary radius")
    private Integer boundaryRadiusMeters;

    @NotNull(message = "Pitch type is required")
    private PitchType pitchType;

    private boolean floodlights;
    private String pavilionDetails;
    private Integer seatingCapacity;

    @NotNull(message = "Hourly base rate is required")
    private BigDecimal hourlyBaseRate;

    private List<String> imageUrls;
    private List<String> facilities;

    public CreateGroundRequest() {
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public List<String> getFacilities() { return facilities; }
    public void setFacilities(List<String> facilities) { this.facilities = facilities; }
}

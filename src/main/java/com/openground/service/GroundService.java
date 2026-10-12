package com.openground.service;

import com.openground.dto.*;
import com.openground.entity.*;
import com.openground.exception.ResourceNotFoundException;
import com.openground.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroundService {

    private final GroundRepository groundRepository;
    private final GroundImageRepository groundImageRepository;
    private final GroundFacilityRepository groundFacilityRepository;
    private final PricingSlotRepository pricingSlotRepository;
    private final ReviewRepository reviewRepository;

    public GroundService(GroundRepository groundRepository,
                         GroundImageRepository groundImageRepository,
                         GroundFacilityRepository groundFacilityRepository,
                         PricingSlotRepository pricingSlotRepository,
                         ReviewRepository reviewRepository) {
        this.groundRepository = groundRepository;
        this.groundImageRepository = groundImageRepository;
        this.groundFacilityRepository = groundFacilityRepository;
        this.pricingSlotRepository = pricingSlotRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional(readOnly = true)
    public List<GroundSummaryDto> searchGrounds(String city, PitchType pitchType, Boolean floodlights,
                                                BigDecimal maxHourlyRate, String search) {
        List<Ground> grounds = groundRepository.searchGrounds(
                city != null && !city.isBlank() ? city.trim() : null,
                pitchType,
                floodlights,
                maxHourlyRate,
                search != null && !search.isBlank() ? search.trim() : null
        );

        return grounds.stream().map(this::toSummaryDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroundDetailDto getGroundById(UUID id) {
        Ground ground = groundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Outdoor cricket ground not found with ID: " + id));
        return toDetailDto(ground);
    }

    @Transactional(readOnly = true)
    public GroundDetailDto getGroundBySlug(String slug) {
        Ground ground = groundRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Outdoor cricket ground not found with slug: " + slug));
        return toDetailDto(ground);
    }

    @Transactional(readOnly = true)
    public List<GroundSummaryDto> getGroundsByOwner(UUID ownerId) {
        return groundRepository.findByOwnerId(ownerId).stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GroundSummaryDto> getAllGroundsForAdmin() {
        return groundRepository.findAll().stream()
                .map(this::toSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public GroundDetailDto createGround(CreateGroundRequest request, User owner) {
        // Enforce strict large outdoor cricket requirement (boundary >= 55m)
        if (request.getBoundaryRadiusMeters() < 55) {
            throw new IllegalArgumentException("OpenGround exclusively lists large outdoor cricket grounds. Minimum boundary radius is 55 meters.");
        }

        String slug = request.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-") + "-" + UUID.randomUUID().toString().substring(0, 6);

        Ground ground = new Ground(
                UUID.randomUUID(),
                owner,
                request.getName(),
                slug,
                request.getDescription(),
                request.getAddress(),
                request.getCity(),
                request.getState(),
                request.getBoundaryRadiusMeters(),
                request.getPitchType(),
                request.isFloodlights(),
                request.getPavilionDetails(),
                request.getSeatingCapacity(),
                request.getHourlyBaseRate(),
                GroundStatus.APPROVED // Default to approved for demo purposes
        );

        Ground saved = groundRepository.save(ground);

        // Add facilities
        if (request.getFacilities() != null) {
            for (String fac : request.getFacilities()) {
                GroundFacility gf = new GroundFacility(UUID.randomUUID(), saved, fac.trim());
                groundFacilityRepository.save(gf);
            }
        }

        // Add images
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            boolean first = true;
            for (String url : request.getImageUrls()) {
                GroundImage gi = new GroundImage(UUID.randomUUID(), saved, url.trim(), first, "Ground photo");
                groundImageRepository.save(gi);
                first = false;
            }
        }

        // Create standard slots
        createStandardPricingSlots(saved, request.getHourlyBaseRate());

        return getGroundById(saved.getId());
    }

    private void createStandardPricingSlots(Ground ground, BigDecimal baseRate) {
        BigDecimal fourHours = baseRate.multiply(BigDecimal.valueOf(4));
        BigDecimal fullDay = baseRate.multiply(BigDecimal.valueOf(9));

        PricingSlot s1 = new PricingSlot(UUID.randomUUID(), ground, SlotType.MORNING, "07:00 AM", "11:30 AM", fourHours, fourHours.multiply(BigDecimal.valueOf(1.2)));
        PricingSlot s2 = new PricingSlot(UUID.randomUUID(), ground, SlotType.AFTERNOON, "12:00 PM", "04:30 PM", fourHours, fourHours.multiply(BigDecimal.valueOf(1.2)));
        PricingSlot s3 = new PricingSlot(UUID.randomUUID(), ground, SlotType.EVENING, "05:00 PM", "09:30 PM", fourHours.multiply(BigDecimal.valueOf(1.4)), fourHours.multiply(BigDecimal.valueOf(1.6)));
        PricingSlot s4 = new PricingSlot(UUID.randomUUID(), ground, SlotType.FULL_DAY, "07:00 AM", "05:00 PM", fullDay, fullDay.multiply(BigDecimal.valueOf(1.25)));

        pricingSlotRepository.saveAll(Arrays.asList(s1, s2, s3, s4));
    }

    @Transactional
    public void updateGroundStatus(UUID groundId, GroundStatus status) {
        Ground ground = groundRepository.findById(groundId)
                .orElseThrow(() -> new ResourceNotFoundException("Ground not found"));
        ground.setStatus(status);
        groundRepository.save(ground);
    }

    public GroundSummaryDto toSummaryDto(Ground g) {
        GroundSummaryDto dto = new GroundSummaryDto();
        dto.setId(g.getId());
        dto.setName(g.getName());
        dto.setSlug(g.getSlug());
        dto.setCity(g.getCity());
        dto.setState(g.getState());
        dto.setAddress(g.getAddress());
        dto.setBoundaryRadiusMeters(g.getBoundaryRadiusMeters());
        dto.setPitchType(g.getPitchType());
        dto.setFloodlights(g.getFloodlights());
        dto.setSeatingCapacity(g.getSeatingCapacity());
        dto.setHourlyBaseRate(g.getHourlyBaseRate());

        // Primary image
        String imgUrl = "https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?auto=format&fit=crop&w=1200&q=80";
        if (g.getImages() != null && !g.getImages().isEmpty()) {
            imgUrl = g.getImages().stream()
                    .filter(GroundImage::getIsPrimary)
                    .map(GroundImage::getImageUrl)
                    .findFirst()
                    .orElse(g.getImages().get(0).getImageUrl());
        }
        dto.setPrimaryImageUrl(imgUrl);

        // Facilities
        List<String> facs = g.getFacilities() != null
                ? g.getFacilities().stream().map(GroundFacility::getFacilityName).limit(3).collect(Collectors.toList())
                : List.of();
        dto.setKeyFacilities(facs);

        // Rating
        double avg = 5.0;
        int count = 0;
        if (g.getReviews() != null && !g.getReviews().isEmpty()) {
            avg = g.getReviews().stream().mapToInt(Review::getRating).average().orElse(5.0);
            count = g.getReviews().size();
        }
        dto.setAverageRating(Math.round(avg * 10.0) / 10.0);
        dto.setReviewCount(count);

        return dto;
    }

    public GroundDetailDto toDetailDto(Ground g) {
        GroundDetailDto dto = new GroundDetailDto();
        dto.setId(g.getId());
        dto.setName(g.getName());
        dto.setSlug(g.getSlug());
        dto.setDescription(g.getDescription());
        dto.setAddress(g.getAddress());
        dto.setCity(g.getCity());
        dto.setState(g.getState());
        dto.setBoundaryRadiusMeters(g.getBoundaryRadiusMeters());
        dto.setPitchType(g.getPitchType());
        dto.setFloodlights(g.getFloodlights());
        dto.setPavilionDetails(g.getPavilionDetails());
        dto.setSeatingCapacity(g.getSeatingCapacity());
        dto.setHourlyBaseRate(g.getHourlyBaseRate());
        dto.setStatus(g.getStatus());
        dto.setCreatedAt(g.getCreatedAt());

        if (g.getImages() != null) {
            dto.setImages(g.getImages().stream()
                    .map(img -> new GroundImageDto(img.getId(), img.getImageUrl(), img.getIsPrimary(), img.getCaption()))
                    .collect(Collectors.toList()));
        } else {
            dto.setImages(List.of());
        }

        if (g.getFacilities() != null) {
            dto.setFacilities(g.getFacilities().stream()
                    .map(GroundFacility::getFacilityName)
                    .collect(Collectors.toList()));
        } else {
            dto.setFacilities(List.of());
        }

        if (g.getSlots() != null) {
            dto.setSlots(g.getSlots().stream()
                    .map(s -> new PricingSlotDto(s.getId(), s.getSlotType(), s.getStartTime(), s.getEndTime(), s.getWeekdayPrice(), s.getWeekendPrice()))
                    .collect(Collectors.toList()));
        } else {
            dto.setSlots(List.of());
        }

        if (g.getReviews() != null) {
            double avg = g.getReviews().stream().mapToInt(Review::getRating).average().orElse(5.0);
            dto.setAverageRating(Math.round(avg * 10.0) / 10.0);
            dto.setReviewCount(g.getReviews().size());
            dto.setReviews(g.getReviews().stream()
                    .map(r -> new ReviewDto(r.getId(), r.getUser().getFullName(), r.getRating(), r.getComment(), r.getCreatedAt()))
                    .collect(Collectors.toList()));
        } else {
            dto.setAverageRating(5.0);
            dto.setReviewCount(0);
            dto.setReviews(List.of());
        }

        return dto;
    }
}

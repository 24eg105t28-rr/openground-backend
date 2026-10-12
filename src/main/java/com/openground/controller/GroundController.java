package com.openground.controller;

import com.openground.dto.GroundDetailDto;
import com.openground.dto.GroundSummaryDto;
import com.openground.entity.PitchType;
import com.openground.service.GroundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grounds")
@Tag(name = "Grounds", description = "Large outdoor cricket ground discovery and details")
public class GroundController {

    private final GroundService groundService;

    public GroundController(GroundService groundService) {
        this.groundService = groundService;
    }

    @GetMapping
    @Operation(summary = "Search large outdoor cricket grounds with optional filters")
    public ResponseEntity<List<GroundSummaryDto>> searchGrounds(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) PitchType pitchType,
            @RequestParam(required = false) Boolean floodlights,
            @RequestParam(required = false) BigDecimal maxHourlyRate,
            @RequestParam(required = false) String search) {

        List<GroundSummaryDto> results = groundService.searchGrounds(city, pitchType, floodlights, maxHourlyRate, search);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get outdoor cricket ground details by ID")
    public ResponseEntity<GroundDetailDto> getGroundById(@PathVariable UUID id) {
        return ResponseEntity.ok(groundService.getGroundById(id));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get outdoor cricket ground details by slug")
    public ResponseEntity<GroundDetailDto> getGroundBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(groundService.getGroundBySlug(slug));
    }
}

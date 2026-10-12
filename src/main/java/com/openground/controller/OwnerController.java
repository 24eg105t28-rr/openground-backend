package com.openground.controller;

import com.openground.dto.BookingResponseDto;
import com.openground.dto.CreateGroundRequest;
import com.openground.dto.GroundDetailDto;
import com.openground.dto.GroundSummaryDto;
import com.openground.entity.User;
import com.openground.service.BookingService;
import com.openground.service.GroundService;
import com.openground.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owner")
@Tag(name = "Ground Owner Operations", description = "Management portal for outdoor cricket ground owners")
@PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
public class OwnerController {

    private final GroundService groundService;
    private final BookingService bookingService;
    private final UserService userService;

    public OwnerController(GroundService groundService,
                           BookingService bookingService,
                           UserService userService) {
        this.groundService = groundService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/grounds")
    @Operation(summary = "Get all outdoor grounds owned by current logged-in owner")
    public ResponseEntity<List<GroundSummaryDto>> getMyGrounds(@AuthenticationPrincipal UserDetails userDetails) {
        User owner = userService.getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(groundService.getGroundsByOwner(owner.getId()));
    }

    @PostMapping("/grounds")
    @Operation(summary = "Submit a new large outdoor cricket ground listing")
    public ResponseEntity<GroundDetailDto> createGround(
            @Valid @RequestBody CreateGroundRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User owner = userService.getByEmail(userDetails.getUsername());
        GroundDetailDto created = groundService.createGround(request, owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/bookings")
    @Operation(summary = "Get all fixture bookings across owner's cricket grounds")
    public ResponseEntity<List<BookingResponseDto>> getOwnerBookings(@AuthenticationPrincipal UserDetails userDetails) {
        User owner = userService.getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(bookingService.getOwnerBookings(owner));
    }
}

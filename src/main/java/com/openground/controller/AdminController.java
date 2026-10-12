package com.openground.controller;

import com.openground.dto.BookingResponseDto;
import com.openground.dto.GroundSummaryDto;
import com.openground.dto.UserDto;
import com.openground.entity.GroundStatus;
import com.openground.service.BookingService;
import com.openground.service.GroundService;
import com.openground.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Administrator Portal", description = "Platform oversight, ground approvals, and all bookings")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final GroundService groundService;
    private final BookingService bookingService;
    private final UserService userService;

    public AdminController(GroundService groundService,
                           BookingService bookingService,
                           UserService userService) {
        this.groundService = groundService;
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/grounds")
    @Operation(summary = "Get all registered outdoor grounds regardless of approval state")
    public ResponseEntity<List<GroundSummaryDto>> getAllGrounds() {
        return ResponseEntity.ok(groundService.getAllGroundsForAdmin());
    }

    @PatchMapping("/grounds/{id}/status")
    @Operation(summary = "Approve or reject an outdoor ground listing")
    public ResponseEntity<Void> updateGroundStatus(
            @PathVariable UUID id,
            @RequestParam GroundStatus status) {
        groundService.updateGroundStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookings")
    @Operation(summary = "Get all match fixture bookings across the platform")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookingsForAdmin());
    }

    @GetMapping("/users")
    @Operation(summary = "Get all registered users across roles")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}

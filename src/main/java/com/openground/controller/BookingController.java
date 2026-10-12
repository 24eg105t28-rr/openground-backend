package com.openground.controller;

import com.openground.dto.*;
import com.openground.entity.User;
import com.openground.service.BookingService;
import com.openground.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@Tag(name = "Bookings", description = "Whole outdoor cricket ground reservation and scheduling")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping("/calculate-price")
    @Operation(summary = "Calculate exact price and check slot availability for an outdoor ground")
    public ResponseEntity<PriceCalculationResponse> calculatePrice(@Valid @RequestBody CalculatePriceRequest request) {
        PriceCalculationResponse response = bookingService.calculatePrice(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Reserve the whole outdoor cricket ground for a match session")
    public ResponseEntity<BookingResponseDto> createBooking(
            @Valid @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getByEmail(userDetails.getUsername());
        BookingResponseDto response = bookingService.createBooking(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    @Operation(summary = "Get bookings of the currently authenticated customer")
    public ResponseEntity<List<BookingResponseDto>> getMyBookings(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByEmail(userDetails.getUsername());
        List<BookingResponseDto> bookings = bookingService.getUserBookings(user);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking and match receipt details by ID")
    public ResponseEntity<BookingResponseDto> getBookingById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getByEmail(userDetails.getUsername());
        BookingResponseDto response = bookingService.getBookingById(id, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a ground booking")
    public ResponseEntity<BookingResponseDto> cancelBooking(
            @PathVariable UUID id,
            @Valid @RequestBody CancelBookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getByEmail(userDetails.getUsername());
        BookingResponseDto response = bookingService.cancelBooking(id, request, user);
        return ResponseEntity.ok(response);
    }
}

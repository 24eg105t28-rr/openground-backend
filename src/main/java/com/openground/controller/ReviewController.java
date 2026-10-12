package com.openground.controller;

import com.openground.dto.CreateReviewRequest;
import com.openground.dto.ReviewDto;
import com.openground.entity.User;
import com.openground.service.ReviewService;
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
@RequestMapping("/api/v1/reviews")
@Tag(name = "Reviews", description = "Match reviews and ground ratings")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/ground/{groundId}")
    @Operation(summary = "Get player and team reviews for a specific outdoor ground")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable UUID groundId) {
        return ResponseEntity.ok(reviewService.getReviewsByGround(groundId));
    }

    @PostMapping
    @Operation(summary = "Submit a review following a played cricket match")
    public ResponseEntity<ReviewDto> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getByEmail(userDetails.getUsername());
        ReviewDto created = reviewService.addReview(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}

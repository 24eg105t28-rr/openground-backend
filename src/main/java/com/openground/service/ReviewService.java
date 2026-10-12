package com.openground.service;

import com.openground.dto.CreateReviewRequest;
import com.openground.dto.ReviewDto;
import com.openground.entity.Booking;
import com.openground.entity.Ground;
import com.openground.entity.Review;
import com.openground.entity.User;
import com.openground.exception.InvalidBookingException;
import com.openground.exception.ResourceNotFoundException;
import com.openground.repository.BookingRepository;
import com.openground.repository.GroundRepository;
import com.openground.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final GroundRepository groundRepository;
    private final BookingRepository bookingRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         GroundRepository groundRepository,
                         BookingRepository bookingRepository) {
        this.reviewRepository = reviewRepository;
        this.groundRepository = groundRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public List<ReviewDto> getReviewsByGround(UUID groundId) {
        return reviewRepository.findByGroundIdOrderByCreatedAtDesc(groundId).stream()
                .map(r -> new ReviewDto(r.getId(), r.getUser().getFullName(), r.getRating(), r.getComment(), r.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDto addReview(CreateReviewRequest request, User user) {
        Ground ground = groundRepository.findById(request.getGroundId())
                .orElseThrow(() -> new ResourceNotFoundException("Ground not found"));

        Booking booking = null;
        if (request.getBookingId() != null) {
            booking = bookingRepository.findById(request.getBookingId()).orElse(null);
            if (reviewRepository.existsByBookingId(request.getBookingId())) {
                throw new InvalidBookingException("A review has already been submitted for this match fixture.");
            }
        }

        Review review = new Review(
                UUID.randomUUID(),
                ground,
                user,
                booking,
                request.getRating(),
                request.getComment()
        );

        Review saved = reviewRepository.save(review);
        return new ReviewDto(saved.getId(), user.getFullName(), saved.getRating(), saved.getComment(), saved.getCreatedAt());
    }
}

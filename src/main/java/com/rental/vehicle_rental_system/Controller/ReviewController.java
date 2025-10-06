package com.rental.vehicle_rental_system.Controller;

import com.rental.vehicle_rental_system.Dto.ReviewDto;
import com.rental.vehicle_rental_system.Entity.Booking;
import com.rental.vehicle_rental_system.Entity.Review;
import com.rental.vehicle_rental_system.Entity.User;
import com.rental.vehicle_rental_system.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/submit")
    public ResponseEntity<Review> submitReview(@RequestBody ReviewDto reviewDto,
                                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        // Convert ReviewDto to Review entity
        Booking booking = new Booking();
        booking.setId(reviewDto.getBookingId());

        Review review = Review.builder()
                .booking(booking)
                .rating(reviewDto.getRating())
                .comment(reviewDto.getComment())
                .build();

        return ResponseEntity.ok(reviewService.submitReview(review, user.getId()));
    }

    @GetMapping("/vehicle/{vehicleId}/reviews")
    public ResponseEntity<List<Review>> getReviewsForVehicleId(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(reviewService.getReviewsForVehicleId(vehicleId));
    }

}

package com.rental.vehicle_rental_system.Service;

import com.rental.vehicle_rental_system.Entity.Booking;
import com.rental.vehicle_rental_system.Entity.BookingStatus;
import com.rental.vehicle_rental_system.Entity.Review;
import com.rental.vehicle_rental_system.Repository.BookingRepository;
import com.rental.vehicle_rental_system.Repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    // reviews

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;

    public Review submitReview(Review review, Long userId) {
        Booking booking = bookingRepository.findById(review.getBooking().getId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));  // Ensure the booking exists related to the review

        if (!booking.getRenter().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to review this booking");
        }

        // Validate that booking is completed
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new RuntimeException("Can only review confirmed bookings");
        }

        // Check if review already exists for this booking
        if (reviewRepository.existsByBookingId(booking.getId())) {
            throw new RuntimeException("Review already submitted for this booking");
        }

        // Validate rating range
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        review.setBooking(booking);     // attach the booking we just fetched to the review object
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForVehicleId(Long vehicleId) {
        return reviewRepository.findByBookingVehicleId(vehicleId);
    }

}

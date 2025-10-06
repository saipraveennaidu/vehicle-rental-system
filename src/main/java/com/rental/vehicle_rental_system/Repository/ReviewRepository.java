package com.rental.vehicle_rental_system.Repository;

import com.rental.vehicle_rental_system.Entity.Review;
import com.rental.vehicle_rental_system.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookingVehicleId(Long vehicleId);     // returns all reviews for a specific vehicle
    boolean existsByBookingId(Long bookingId);
}

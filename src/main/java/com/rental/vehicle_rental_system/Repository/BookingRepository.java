package com.rental.vehicle_rental_system.Repository;

import com.rental.vehicle_rental_system.Entity.Booking;
import com.rental.vehicle_rental_system.Entity.BookingStatus;
import com.rental.vehicle_rental_system.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRenterId(Long renterId);    // returns all bookings made by a specific renter
    List<Booking> findByVehicleOwnerId(Long vehicleOwnerId);   // returns all bookings for vehicles owned by a specific user
    List<Booking> findByVehicleIdAndStatusIn(Long vehicleId, List<BookingStatus> statuses);     // returns bookings for a vehicle with specific statuses
}

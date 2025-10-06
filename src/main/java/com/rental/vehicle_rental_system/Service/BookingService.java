package com.rental.vehicle_rental_system.Service;

import com.rental.vehicle_rental_system.Entity.Booking;
import com.rental.vehicle_rental_system.Entity.BookingStatus;
import com.rental.vehicle_rental_system.Entity.User;
import com.rental.vehicle_rental_system.Entity.Vehicle;
import com.rental.vehicle_rental_system.Repository.BookingRepository;
import com.rental.vehicle_rental_system.Repository.UserRepository;
import com.rental.vehicle_rental_system.Repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    // send booking request
    // approve/reject booking
    // view bookings (for both renters and owners)

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public Booking requestBooking(Booking booking) {
        Vehicle vehicle = vehicleRepository.findById(booking.getVehicle().getId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (!vehicle.isAvailability()) {
            throw new RuntimeException("Vehicle is not available for booking");
        }

        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

    public Booking findBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public Booking approveBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    public Booking rejectBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getOwnerBookings(Long ownerId) {
        return bookingRepository.findByVehicleOwnerId(ownerId);
    }

    public List<Booking> getRenterBookings(Long renterId) {
        return bookingRepository.findByRenterId(renterId);
    }

}

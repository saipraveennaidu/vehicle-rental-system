package com.rental.vehicle_rental_system.Controller;

import com.rental.vehicle_rental_system.Dto.BookingDto;
import com.rental.vehicle_rental_system.Entity.Booking;
import com.rental.vehicle_rental_system.Entity.BookingStatus;
import com.rental.vehicle_rental_system.Entity.User;
import com.rental.vehicle_rental_system.Entity.Vehicle;
import com.rental.vehicle_rental_system.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> requestBooking(@RequestBody BookingDto bookingDto,
                                                  Authentication authentication) {
        User renter = (User) authentication.getPrincipal();

        Vehicle vehicle = Vehicle.builder()
                .id(bookingDto.getVehicleId())
                .build();

        Booking booking =  Booking.builder()
                .vehicle(vehicle)
                .renter(renter)
                .startDateTime(bookingDto.getStartDateTime())
                .endDateTime(bookingDto.getEndDateTime())
                .status(BookingStatus.PENDING)
                .build();

        return ResponseEntity.ok(bookingService.requestBooking(booking));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<Booking> approveBooking(@PathVariable Long id,
                                                  Authentication authentication) {
        User owner = (User) authentication.getPrincipal();
        Booking booking = bookingService.findBookingById(id);

        if (!booking.getVehicle().getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You are not authorized to approve this booking");
        }

        return ResponseEntity.ok(bookingService.approveBooking(id));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize(("hasAuthority('OWNER')"))
    public ResponseEntity<Booking> rejectBooking(@PathVariable Long id,
                                                 Authentication authentication) {
        User owner = (User) authentication.getPrincipal();
        Booking booking = bookingService.findBookingById(id);

        if (!booking.getVehicle().getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You are not authorized to reject this booking");
        }

        return ResponseEntity.ok(bookingService.rejectBooking(id));
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<List<Booking>> getOwnerBookings(@PathVariable Long ownerId,
                                                          Authentication authentication) {
        User owner = (User) authentication.getPrincipal();

        if (!ownerId.equals(owner.getId())) {
            throw new RuntimeException("You are not authorized to view these bookings");
        }

        return ResponseEntity.ok(bookingService.getOwnerBookings(ownerId));
    }

    @GetMapping("/renter/{renterId}")
    @PreAuthorize(("hasAuthority('RENTER')"))
    public ResponseEntity<List<Booking>> getRenterBookings(@PathVariable Long renterId,
                                                           Authentication authentication) {
        User renter = (User) authentication.getPrincipal();

        if (!renterId.equals(renter.getId())) {
            throw new RuntimeException("You are not authorized to view these bookings");
        }

        return ResponseEntity.ok(bookingService.getRenterBookings(renterId));
    }

}

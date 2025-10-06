package com.rental.vehicle_rental_system.Controller;

import com.rental.vehicle_rental_system.Dto.VehicleDto;
import com.rental.vehicle_rental_system.Entity.User;
import com.rental.vehicle_rental_system.Entity.Vehicle;
import com.rental.vehicle_rental_system.Service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<Vehicle> addVehicle(@ModelAttribute VehicleDto vehicleDto,
                                              @RequestParam("photo") MultipartFile photo,
                                              Authentication authentication) throws IOException {

        User owner = (User) authentication.getPrincipal();

        System.out.println("Authenticated user: " + authentication);

        Vehicle vehicle = Vehicle.builder()
                .name(vehicleDto.getName())
                .model(vehicleDto.getModel())
                .type(vehicleDto.getType())
                .price(vehicleDto.getPrice())
                .location(vehicleDto.getLocation())
                .availability(vehicleDto.isAvailability())
                .owner(owner)
                .photo(photo.getBytes())
                .build();

        return ResponseEntity.ok(vehicleService.addVehicle(vehicle));
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<Vehicle> editVehicle(@PathVariable Long id,
                                               @ModelAttribute VehicleDto vehicleDto,
                                               @RequestParam(value = "photo", required = false) MultipartFile photo,
                                               Authentication authentication) throws IOException {

        User owner = (User) authentication.getPrincipal();

        System.out.println("Authenticated user: " + authentication);

        Vehicle existingVehicle = vehicleService.findVehicleById(id);

        if(!existingVehicle.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("Access denied: You can only edit your own vehicles.");
        }

        Vehicle vehicleDetails = Vehicle.builder()
                .name(vehicleDto.getName())
                .model(vehicleDto.getModel())
                .type(vehicleDto.getType())
                .price(vehicleDto.getPrice())
                .location(vehicleDto.getLocation())
                .availability(vehicleDto.isAvailability())
                .photo(photo != null ? photo.getBytes() : null)
                .owner(owner)
                .build();

        return ResponseEntity.ok(vehicleService.editVehicle(id, vehicleDetails));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long id,
                                                Authentication authentication) {
        User owner = (User) authentication.getPrincipal();
        Vehicle existingVehicle = vehicleService.findVehicleById(id);

        System.out.println("Authenticated user: " + authentication);

        if(!existingVehicle.getOwner().getId().equals(owner.getId())) {
            return ResponseEntity.status(403).body("Access denied: You can only delete your own vehicles.");
        }

        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }

    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<Vehicle> toogleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.toggleAvailability(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Vehicle>> filterVehicles(
            @RequestParam String type,
            @RequestParam String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return ResponseEntity.ok(vehicleService.filterVehicles(type, location,
                minPrice != null ? minPrice : 0.0,
                maxPrice != null ? maxPrice : Double.MAX_VALUE
        ));
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAuthority('OWNER')")
    public ResponseEntity<List<Vehicle>> getVehiclesByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(vehicleService.findByOwnerId(ownerId));
    }

}

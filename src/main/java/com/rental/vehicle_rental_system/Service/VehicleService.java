package com.rental.vehicle_rental_system.Service;

import com.rental.vehicle_rental_system.Entity.Vehicle;
import com.rental.vehicle_rental_system.Repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    // add, edit, delete vehicle
    // toggle availability
    // filter vehicles by type, location, price range

    private final VehicleRepository vehicleRepository;

    public Vehicle addVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle editVehicle(Long id, Vehicle vehicleDetails) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        vehicle.setName(vehicleDetails.getName());
        vehicle.setModel(vehicleDetails.getModel());
        vehicle.setType(vehicleDetails.getType());
        vehicle.setPrice(vehicleDetails.getPrice());
        vehicle.setLocation(vehicleDetails.getLocation());
        vehicle.setAvailability(vehicleDetails.isAvailability());
        vehicle.setPhoto(vehicleDetails.getPhoto());
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found");
        }
        vehicleRepository.deleteById(id);
    }

    public Vehicle toggleAvailability(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        vehicle.setAvailability(!vehicle.isAvailability());
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> filterVehicles(String type, String location, Double minPrice, Double maxPrice) {
        return vehicleRepository.findByTypeAndLocationAndPriceBetween(type, location, minPrice, maxPrice);
    }

    public Vehicle findVehicleById(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public List<Vehicle> findByOwnerId(Long ownerId) {
        return vehicleRepository.findByOwnerId(ownerId);
    }

}

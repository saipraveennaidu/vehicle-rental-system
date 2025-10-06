package com.rental.vehicle_rental_system.Repository;

import com.rental.vehicle_rental_system.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByTypeAndLocationAndPriceBetween(String type, String location, Double minPrice, Double maxPrice);
    List<Vehicle> findByOwnerId(Long ownerId);
}

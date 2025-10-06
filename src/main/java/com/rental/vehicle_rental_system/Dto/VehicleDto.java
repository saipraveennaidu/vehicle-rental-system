package com.rental.vehicle_rental_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private String name;
    private String model;
    private String type;
    private Double price;
    private String location;
    private boolean availability;
//    private byte[] photo; // Base64 encoded string
}

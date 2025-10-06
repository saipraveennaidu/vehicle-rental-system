package com.rental.vehicle_rental_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String name;
    private String email;
    private String password;
    private String role; // "OWNER" or "RENTER"
//    private byte[] governmentId; // Base64 encoded string
}

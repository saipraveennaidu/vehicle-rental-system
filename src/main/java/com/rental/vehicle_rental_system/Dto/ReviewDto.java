package com.rental.vehicle_rental_system.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long bookingId;
    private int rating;
    private String comment;
}

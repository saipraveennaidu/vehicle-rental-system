package com.rental.vehicle_rental_system.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDto {
    private Long vehicleId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}

package com.rental.vehicle_rental_system.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String model;
    private String type;
    private Double price;
    private String location;
    private boolean availability;

    @Lob
    private byte[] photo;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

}

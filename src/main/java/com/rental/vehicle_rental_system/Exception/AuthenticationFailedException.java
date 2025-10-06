package com.rental.vehicle_rental_system.Exception;

public class AuthenticationFailedException extends RuntimeException{
    public AuthenticationFailedException(String message) {
        super(message);
    }
}

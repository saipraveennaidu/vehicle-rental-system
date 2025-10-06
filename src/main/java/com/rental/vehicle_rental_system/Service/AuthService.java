package com.rental.vehicle_rental_system.Service;

import com.rental.vehicle_rental_system.Dto.AuthRequest;
import com.rental.vehicle_rental_system.Dto.AuthResponse;
import com.rental.vehicle_rental_system.Entity.Role;
import com.rental.vehicle_rental_system.Entity.User;
import com.rental.vehicle_rental_system.Repository.UserRepository;
import com.rental.vehicle_rental_system.Security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    // register new user
    // login existing user

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthResponse register(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole() == null) user.setRole(Role.RENTER);
        userRepository.save(user);
        String token = jwtTokenUtil.generateToken(user.getEmail());
        return new AuthResponse(token);     // Return AuthResponse obj instead of String bcoz, we might want to add more fields in future
    }

    public AuthResponse login(AuthRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty() ||
                !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        User user = userOptional.get();
        String token = jwtTokenUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

}

package com.rental.vehicle_rental_system.Controller;

import com.rental.vehicle_rental_system.Dto.AuthRequest;
import com.rental.vehicle_rental_system.Dto.AuthResponse;
import com.rental.vehicle_rental_system.Dto.UserDto;
import com.rental.vehicle_rental_system.Entity.Role;
import com.rental.vehicle_rental_system.Entity.User;
import com.rental.vehicle_rental_system.Exception.AuthenticationFailedException;
import com.rental.vehicle_rental_system.Security.JwtTokenUtil;
import com.rental.vehicle_rental_system.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<AuthResponse> registerUser(@ModelAttribute UserDto userDto,
                                                     @RequestParam("governmentId") MultipartFile governmentId) throws IOException {
        if (userDto.getName() == null || userDto.getEmail() == null ||
                userDto.getPassword() == null || userDto.getRole() == null) {
            throw new IllegalArgumentException("Invalid input: All fields are required.");
        }

        byte[] govIdBytes = governmentId.getBytes();

        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .role(Role.valueOf(userDto.getRole().toUpperCase()))
                .governmentId(govIdBytes)
                .build();

        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            String token = jwtTokenUtil.generateToken(request.getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            // throw your custom exception
            throw new AuthenticationFailedException("Invalid username or password");
        }
    }

}

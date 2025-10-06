package com.rental.vehicle_rental_system.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.rental.vehicle_rental_system.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip JWT check for public endpoints
        if (path.startsWith("/api/auth/")
                || path.startsWith("/auth/")
                || path.equals("/register")
                || path.equals("/vehicles/filter")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String token = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtTokenUtil.extractUsername(token);
            } catch (Exception e) {
                // Invalid token
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if(jwtTokenUtil.validateToken(token, userDetails))
            userRepository.findByEmail(email).ifPresent(user -> {
                List<SimpleGrantedAuthority> authorities = user.getRole() != null ?
                        List.of(new SimpleGrantedAuthority(user.getRole().name())) : List.of();

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
            });
        }

        filterChain.doFilter(request, response);

    }
}

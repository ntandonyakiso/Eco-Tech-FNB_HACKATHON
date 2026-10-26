package com.ecotech.backend.controller;

import com.ecotech.backend.dto.AuthRequest;
import com.ecotech.backend.dto.AuthResponse;
import com.ecotech.backend.dto.ErrorResponse;
import com.ecotech.backend.domain.User;
import com.ecotech.backend.domain.Ward;
import com.ecotech.backend.repository.UserRepository;
import com.ecotech.backend.repository.WardRepository;
import com.ecotech.backend.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final WardRepository wardRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          WardRepository wardRepository,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.wardRepository = wardRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if ("EMAIL".equals(request.getType().name())) {
            if (userRepository.existsByEmail(request.getIdentifier())) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(400, "BAD_REQUEST", "Email already registered", "/api/v1/auth/register"));
            }
        } else {
            if (userRepository.existsByPhone(request.getIdentifier())) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse(400, "BAD_REQUEST", "Phone already registered", "/api/v1/auth/register"));
            }
        }

        Optional<Ward> ward = wardRepository.findAll().stream()
                .filter(w -> w.getName().equalsIgnoreCase(request.getWardName()))
                .findFirst();

        if (ward.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse(400, "BAD_REQUEST", "Ward not found: " + request.getWardName(), "/api/v1/auth/register"));
        }

        User user = new User();
        user.setEmail("EMAIL".equals(request.getType().name()) ? request.getIdentifier() : null);
        user.setPhone("PHONE".equals(request.getType().name()) ? request.getIdentifier() : null);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.RESIDENT);
        user.setWardId(ward.get().getId());
        user.setLanguage(request.getLanguage());
        user.setPoipiaConsent(true);
        user.setPoipiaConsentAt(Instant.now());
        user = userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail() != null ? user.getEmail() : user.getPhone())
                .password(user.getPasswordHash())
                .roles("")
                .build();

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(
                token, user.getId().toString(), user.getRole().name(),
                user.getWardId().toString(), user.getLanguage()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword()));

        User user = userRepository.findByEmail(request.getIdentifier())
                .or(() -> userRepository.findByPhone(request.getIdentifier()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail() != null ? user.getEmail() : user.getPhone())
                .password(user.getPasswordHash())
                .roles("")
                .build();

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(
                token, user.getId().toString(), user.getRole().name(),
                user.getWardId().toString(), user.getLanguage()));
    }
}

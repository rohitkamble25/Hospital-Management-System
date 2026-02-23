package com.hms.hospital_management_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.hms.hospital_management_backend.Repo.UserRepo;
import com.hms.hospital_management_backend.model.User;
import com.hms.hospital_management_backend.service.AuthService;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepo userRepo;

    public AuthController(AuthService authService, UserRepo userRepo) {
        this.authService = authService;
        this.userRepo = userRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        return ResponseEntity.ok(
                authService.login(user.getEmail(), user.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(
                authService.register(
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getRole()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null)
            return ResponseEntity.status(401).build();

        Map<String, Object> response = new HashMap<>();
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());

        if (user.getDoctor() != null)
            response.put("doctorId", user.getDoctor().getId());

        if (user.getPatient() != null)
            response.put("patientId", user.getPatient().getId());

        if (user.getReceptionist() != null)
            response.put("receptionistId", user.getReceptionist().getId());

        return ResponseEntity.ok(response);
    }
}

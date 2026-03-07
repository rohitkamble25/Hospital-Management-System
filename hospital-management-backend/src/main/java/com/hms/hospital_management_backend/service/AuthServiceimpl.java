package com.hms.hospital_management_backend.service;

import com.hms.hospital_management_backend.model.Role;
import com.hms.hospital_management_backend.model.Patient;
import com.hms.hospital_management_backend.Repo.UserRepo;
import com.hms.hospital_management_backend.Repo.PatientRepo;
import com.hms.hospital_management_backend.model.User;
import com.hms.hospital_management_backend.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceimpl implements AuthService {

    private final UserRepo userRepo;
    private final PatientRepo patientRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceimpl(UserRepo userRepo,
            PatientRepo patientRepo,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.userRepo = userRepo;
        this.patientRepo = patientRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public String login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return jwtService.generateToken(user);
    }

    @Override
    public User register(String name, String email, String password, Role role) {

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.PATIENT)
                .build();

        user = userRepo.save(user);

        Patient patient = Patient.builder()
                .name(name)
                .user(user)
                .build();

        patientRepo.save(patient);

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
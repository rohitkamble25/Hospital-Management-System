package com.hms.hospital_management_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.hms.hospital_management_backend.model.Appointment;
import com.hms.hospital_management_backend.model.Doctor;
import com.hms.hospital_management_backend.model.User;
import com.hms.hospital_management_backend.Repo.UserRepo;
import com.hms.hospital_management_backend.Repo.DoctorRepo;
import com.hms.hospital_management_backend.service.AppointmentService;

@RestController
@CrossOrigin
@RequestMapping("/api/doctor")
public class DoctorController {

    private final AppointmentService appointmentService;
    private final UserRepo userRepo;
    private final DoctorRepo doctorRepo;

    public DoctorController(
            AppointmentService appointmentService,
            UserRepo userRepo,
            DoctorRepo doctorRepo) {
        this.appointmentService = appointmentService;
        this.userRepo = userRepo;
        this.doctorRepo = doctorRepo;
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getMyAppointments(Authentication auth) {

        String email = auth.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = doctorRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return ResponseEntity.ok(
                appointmentService.getByDoctor(doctor.getId()));
    }
}

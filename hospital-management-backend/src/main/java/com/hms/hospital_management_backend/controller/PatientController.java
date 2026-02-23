package com.hms.hospital_management_backend.controller;

import com.hms.hospital_management_backend.model.Appointment;
import com.hms.hospital_management_backend.model.Bill;
import com.hms.hospital_management_backend.model.Patient;
import com.hms.hospital_management_backend.model.User;
import com.hms.hospital_management_backend.model.CreatePatientRequest;
import com.hms.hospital_management_backend.Repo.PatientRepo;
import com.hms.hospital_management_backend.Repo.UserRepo;
import com.hms.hospital_management_backend.service.AppointmentService;
import com.hms.hospital_management_backend.service.BillService;
import com.hms.hospital_management_backend.service.PatientService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin
public class PatientController {

    private final AppointmentService appointmentService;

    private final BillService billService;
    private final UserRepo userRepo;
    private final PatientRepo patientRepo;
    private final PatientService patientService;

    public PatientController(AppointmentService appointmentService,

            BillService billService,
            UserRepo userRepo,
            PatientRepo patientRepo,
            PatientService patientService) {
        this.appointmentService = appointmentService;

        this.billService = billService;
        this.userRepo = userRepo;
        this.patientRepo = patientRepo;
        this.patientService = patientService;
    }

    @PostMapping("/register")
    public ResponseEntity<Patient> register(@RequestBody CreatePatientRequest request) {
        return ResponseEntity.ok(patientService.create(request));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getMyAppointments(Authentication auth) {

        String email = auth.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = patientRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return ResponseEntity.ok(
                appointmentService.getByPatient(patient.getId()));
    }

    @GetMapping("/bills")
    public ResponseEntity<List<Bill>> getMyBills(Authentication auth) {

        String email = auth.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Patient patient = patientRepo.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return ResponseEntity.ok(
                billService.getByPatient(patient.getId()));
    }
}

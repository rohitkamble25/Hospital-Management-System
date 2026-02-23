package com.hms.hospital_management_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hms.hospital_management_backend.model.CreateDoctorRequest;
import com.hms.hospital_management_backend.model.CreateReceptionistRequest;
import com.hms.hospital_management_backend.model.Doctor;
import com.hms.hospital_management_backend.model.Patient;
import com.hms.hospital_management_backend.model.Receptionist;
import com.hms.hospital_management_backend.service.DoctorService;
import com.hms.hospital_management_backend.service.PatientService;
import com.hms.hospital_management_backend.service.ReceptionistService;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdminController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final ReceptionistService receptionistService;

    public AdminController(
            DoctorService doctorService,
            PatientService patientService,
            ReceptionistService receptionistService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.receptionistService = receptionistService;
    }

    @PostMapping("/doctors")
    public ResponseEntity<Doctor> addDoctor(@RequestBody CreateDoctorRequest request) {
        return ResponseEntity.ok(doctorService.create(request));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getDoctors() {
        return ResponseEntity.ok(doctorService.getAll());
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatients() {
        return ResponseEntity.ok(patientService.getAll());
    }

    @PostMapping("/receptionists")
    public ResponseEntity<Receptionist> addReceptionist(
            @RequestBody CreateReceptionistRequest request) {
        return ResponseEntity.ok(receptionistService.create(request));
    }

    @GetMapping("/receptionists")
    public ResponseEntity<List<Receptionist>> getReceptionists() {
        return ResponseEntity.ok(receptionistService.getAll());
    }

    @DeleteMapping("/receptionists/{id}")
    public ResponseEntity<Void> deleteReceptionist(@PathVariable Long id) {
        receptionistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

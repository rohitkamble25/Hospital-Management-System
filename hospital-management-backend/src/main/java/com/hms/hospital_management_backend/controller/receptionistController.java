package com.hms.hospital_management_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hms.hospital_management_backend.model.Appointment;
import com.hms.hospital_management_backend.model.Bill;
import com.hms.hospital_management_backend.model.CreatePatientRequest;
import com.hms.hospital_management_backend.model.Patient;
import com.hms.hospital_management_backend.service.AppointmentService;
import com.hms.hospital_management_backend.service.BillService;
import com.hms.hospital_management_backend.service.PatientService;

@RestController
@CrossOrigin
@RequestMapping("/api/reception")
public class receptionistController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final BillService billService;

    public receptionistController(
            PatientService patientService,
            AppointmentService appointmentService,
            BillService billService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.billService = billService;
    }

    @PostMapping("/patients")
    public Patient addPatient(@RequestBody CreatePatientRequest request) {
        return patientService.create(request);
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatients() {
        return ResponseEntity.ok(patientService.getAll());
    }

    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        return ResponseEntity.ok(appointmentService.create(appointment));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAppointments() {
        return ResponseEntity.ok(appointmentService.getAll());
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bills")
    public ResponseEntity<Bill> createBill(
            @RequestParam Long patientId,
            @RequestBody Bill bill) {
        return ResponseEntity.ok(billService.create(patientId, bill));
    }

    @GetMapping("/bills")
    public ResponseEntity<List<Bill>> getBills(@RequestParam Long patientId) {
        return ResponseEntity.ok(billService.getByPatient(patientId));
    }

    @DeleteMapping("/bills/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.delete(id);
        return ResponseEntity.ok().build();
    }
}

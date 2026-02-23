package com.hms.hospital_management_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.hospital_management_backend.Repo.AppointmentRepo;
import com.hms.hospital_management_backend.Repo.DoctorRepo;
import com.hms.hospital_management_backend.Repo.PatientRepo;
import com.hms.hospital_management_backend.model.*;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;

    public AppointmentServiceImpl(
            AppointmentRepo appointmentRepo,
            DoctorRepo doctorRepo,
            PatientRepo patientRepo) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
    }

    @Override
    public Appointment create(Appointment appointment) {

        Patient patient = patientRepo
                .findById(appointment.getPatient().getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepo
                .findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        if (appointment.getStatus() == null) {
            appointment.setStatus(AppointmentStatus.SCHEDULED);
        }

        return appointmentRepo.save(appointment);
    }

    @Override
    public Appointment updateStatus(Long id, String status) {

        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        try {
            appointment.setStatus(AppointmentStatus.valueOf(status));
        } catch (Exception e) {
            throw new RuntimeException("Invalid appointment status");
        }

        return appointmentRepo.save(appointment);
    }

    @Override
    public List<Appointment> getByDoctor(Long doctorId) {

        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return appointmentRepo.findByDoctor(doctor);
    }

    @Override
    public List<Appointment> getByPatient(Long patientId) {

        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return appointmentRepo.findByPatient(patient);
    }

    @Override
    public List<Appointment> getAll() {
        return appointmentRepo.findAll();
    }

    @Override
    public void delete(Long id) {
        appointmentRepo.deleteById(id);
    }
}

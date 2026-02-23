package com.hms.hospital_management_backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.hospital_management_backend.Repo.PatientRepo;
import com.hms.hospital_management_backend.Repo.UserRepo;
import com.hms.hospital_management_backend.model.CreatePatientRequest;
import com.hms.hospital_management_backend.model.Patient;
import com.hms.hospital_management_backend.model.Role;
import com.hms.hospital_management_backend.model.User;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepo patientRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public PatientServiceImpl(PatientRepo patientRepo,
            UserRepo userRepo,
            PasswordEncoder passwordEncoder) {
        this.patientRepo = patientRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Patient create(CreatePatientRequest req) {

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.PATIENT);

        User savedUser = userRepo.save(user);

        Patient patient = new Patient();
        patient.setName(req.getName());
        patient.setAge(req.getAge());
        patient.setPhone(req.getPhone());
        patient.setAddress(req.getAddress());
        patient.setMedicalHistory(req.getMedicalHistory());
        patient.setUser(savedUser);

        return patientRepo.save(patient);
    }

    @Override
    public Patient update(Long id, Patient patient) {
        Patient existing = getById(id);
        existing.setName(patient.getName());
        existing.setAge(patient.getAge());
        existing.setPhone(patient.getPhone());
        existing.setAddress(patient.getAddress());
        existing.setMedicalHistory(patient.getMedicalHistory());
        return patientRepo.save(existing);
    }

    @Override
    public Patient getById(Long id) {
        return patientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    @Override
    public List<Patient> getAll() {
        return patientRepo.findAll();
    }

    @Override
    public void delete(Long id) {
        patientRepo.deleteById(id);
    }
}

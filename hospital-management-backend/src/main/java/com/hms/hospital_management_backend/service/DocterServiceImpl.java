package com.hms.hospital_management_backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hms.hospital_management_backend.Repo.DoctorRepo;
import com.hms.hospital_management_backend.Repo.UserRepo;
import com.hms.hospital_management_backend.model.CreateDoctorRequest;
import com.hms.hospital_management_backend.model.Doctor;
import com.hms.hospital_management_backend.model.Role;
import com.hms.hospital_management_backend.model.User;

@Service
public class DocterServiceImpl implements DoctorService {

    private final DoctorRepo doctorRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public DocterServiceImpl(
            DoctorRepo doctorRepo,
            UserRepo userRepo,
            PasswordEncoder passwordEncoder) {
        this.doctorRepo = doctorRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Doctor create(CreateDoctorRequest req) {

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.DOCTOR);

        User savedUser = userRepo.save(user);

        Doctor doctor = new Doctor();
        doctor.setName(req.getName());
        doctor.setSpecialization(req.getSpecialization());
        doctor.setUser(savedUser);

        return doctorRepo.save(doctor);
    }

    @Override
    public Doctor update(Long id, Doctor doctor) {
        Doctor existing = getById(id);
        existing.setName(doctor.getName());
        existing.setSpecialization(doctor.getSpecialization());
        return doctorRepo.save(existing);
    }

    @Override
    public Doctor getById(Long id) {
        return doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    @Override
    public List<Doctor> getAll() {
        return doctorRepo.findAll();
    }

    @Override
    public void delete(Long id) {
        doctorRepo.deleteById(id);
    }
}

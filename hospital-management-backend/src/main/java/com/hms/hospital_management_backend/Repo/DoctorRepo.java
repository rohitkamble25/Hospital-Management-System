package com.hms.hospital_management_backend.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.hospital_management_backend.model.Doctor;

public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserId(Long userId);

}

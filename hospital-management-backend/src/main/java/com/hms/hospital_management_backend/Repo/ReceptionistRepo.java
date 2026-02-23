package com.hms.hospital_management_backend.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.hospital_management_backend.model.Receptionist;

public interface ReceptionistRepo extends JpaRepository<Receptionist, Long> {
    Optional<Receptionist> findByUserId(Long userId);
}

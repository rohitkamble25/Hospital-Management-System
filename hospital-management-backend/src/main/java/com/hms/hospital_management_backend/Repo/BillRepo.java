package com.hms.hospital_management_backend.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.hospital_management_backend.model.Bill;
import com.hms.hospital_management_backend.model.Patient;

public interface BillRepo extends JpaRepository<Bill, Long> {
    List<Bill> findByPatient(Patient patient);
}

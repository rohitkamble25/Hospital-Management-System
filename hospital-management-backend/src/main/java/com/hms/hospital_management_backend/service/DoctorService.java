package com.hms.hospital_management_backend.service;

import java.util.List;

import com.hms.hospital_management_backend.model.CreateDoctorRequest;
import com.hms.hospital_management_backend.model.Doctor;

public interface DoctorService {

    Doctor create(CreateDoctorRequest request);

    Doctor update(Long id, Doctor doctor);

    Doctor getById(Long id);

    List<Doctor> getAll();

    void delete(Long id);
}

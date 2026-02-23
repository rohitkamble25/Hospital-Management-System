package com.hms.hospital_management_backend.service;

import java.util.List;

import com.hms.hospital_management_backend.model.CreatePatientRequest;
import com.hms.hospital_management_backend.model.Patient;

public interface PatientService {

    Patient create(CreatePatientRequest request);

    Patient update(Long id, Patient patient);

    Patient getById(Long id);

    List<Patient> getAll();

    void delete(Long id);
}

package com.hms.hospital_management_backend.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hms.hospital_management_backend.model.Appointment;
import com.hms.hospital_management_backend.model.Doctor;
import com.hms.hospital_management_backend.model.Patient;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctor(Doctor doctor);

    List<Appointment> findByPatient(Patient patient);
}

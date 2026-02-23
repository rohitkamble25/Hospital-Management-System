package com.hms.hospital_management_backend.service;

import java.util.List;

import com.hms.hospital_management_backend.model.Appointment;

public interface AppointmentService {

    Appointment create(Appointment appointment);

    Appointment updateStatus(Long id, String status);

    List<Appointment> getByDoctor(Long doctorId);

    List<Appointment> getByPatient(Long patientId);

    List<Appointment> getAll();

    void delete(Long id);
}

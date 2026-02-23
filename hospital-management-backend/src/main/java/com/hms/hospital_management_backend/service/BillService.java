package com.hms.hospital_management_backend.service;

import java.util.List;

import com.hms.hospital_management_backend.model.Bill;

public interface BillService {

    Bill create(Long patientId, Bill bill);

    List<Bill> getByPatient(Long patientId);

    void delete(Long id);
}

package com.hms.hospital_management_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hms.hospital_management_backend.Repo.BillRepo;
import com.hms.hospital_management_backend.Repo.PatientRepo;
import com.hms.hospital_management_backend.model.Bill;
import com.hms.hospital_management_backend.model.Patient;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepo billRepo;
    private final PatientRepo patientRepo;

    public BillServiceImpl(BillRepo billRepo, PatientRepo patientRepo) {
        this.billRepo = billRepo;
        this.patientRepo = patientRepo;
    }

    @Override
    public Bill create(Long patientId, Bill bill) {

        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        bill.setPatient(patient);
        bill.setCreatedAt(LocalDateTime.now());

        return billRepo.save(bill);
    }

    @Override
    public List<Bill> getByPatient(Long patientId) {
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return billRepo.findByPatient(patient);
    }

    @Override
    public void delete(Long id) {
        billRepo.deleteById(id);
    }

}

package com.hms.hospital_management_backend.service;

import java.util.List;

import com.hms.hospital_management_backend.model.CreateReceptionistRequest;
import com.hms.hospital_management_backend.model.Receptionist;

public interface ReceptionistService {

    List<Receptionist> getAll();

    void delete(Long id);

    Receptionist create(CreateReceptionistRequest req);
}

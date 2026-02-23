package com.hms.hospital_management_backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hms.hospital_management_backend.Repo.ReceptionistRepo;
import com.hms.hospital_management_backend.Repo.UserRepo;
import com.hms.hospital_management_backend.model.CreateReceptionistRequest;
import com.hms.hospital_management_backend.model.Receptionist;
import com.hms.hospital_management_backend.model.Role;
import com.hms.hospital_management_backend.model.User;

@Service
@Transactional
public class ReceptionistServiceImpl implements ReceptionistService {

    private final ReceptionistRepo receptionistRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public ReceptionistServiceImpl(
            ReceptionistRepo receptionistRepo,
            UserRepo userRepo,
            PasswordEncoder passwordEncoder) {
        this.receptionistRepo = receptionistRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Receptionist create(CreateReceptionistRequest req) {

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(Role.RECEPTIONIST);

        User savedUser = userRepo.save(user);

        Receptionist receptionist = new Receptionist();
        receptionist.setName(req.getName());
        receptionist.setUser(savedUser);

        return receptionistRepo.save(receptionist);
    }

    @Override
    public List<Receptionist> getAll() {
        return receptionistRepo.findAll();
    }

    @Override
    public void delete(Long id) {

        Receptionist receptionist = receptionistRepo
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Receptionist not found"));

        User user = receptionist.getUser();

        receptionistRepo.delete(receptionist);
        userRepo.delete(user);
    }
}

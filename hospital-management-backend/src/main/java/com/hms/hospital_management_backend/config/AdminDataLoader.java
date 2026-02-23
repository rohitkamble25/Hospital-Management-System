package com.hms.hospital_management_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hms.hospital_management_backend.model.User;
import com.hms.hospital_management_backend.model.Role;
import com.hms.hospital_management_backend.Repo.UserRepo;

@Configuration
public class AdminDataLoader {

    @Bean
    CommandLineRunner seedAdmin(UserRepo userRepo,
            PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepo.existsByRole(Role.ADMIN)) {
                return;
            }

            User admin = new User();
            admin.setName("Super Admin");
            admin.setEmail("admin@hms.com");
            admin.setPassword(passwordEncoder.encode("admin@3223"));
            admin.setRole(Role.ADMIN);

            userRepo.save(admin);

            System.out.println("✅ Admin account created");
        };
    }
}

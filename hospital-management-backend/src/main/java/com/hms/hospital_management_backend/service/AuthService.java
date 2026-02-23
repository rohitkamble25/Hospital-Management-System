package com.hms.hospital_management_backend.service;

import com.hms.hospital_management_backend.model.Role;
import com.hms.hospital_management_backend.model.User;

public interface AuthService {

    String login(String email, String password);

    User register(String name, String email, String password, Role role);

    User getUserByEmail(String email);
}

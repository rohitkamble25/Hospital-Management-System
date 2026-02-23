package com.hms.hospital_management_backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDoctorRequest {
    private String name;
    private String specialization;
    private String email;
    private String password;
}

package com.hms.hospital_management_backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePatientRequest {

    private String name;
    private Integer age;
    private String phone;
    private String address;
    private String medicalHistory;

    private String email;
    private String password;
}

package com.hms.hospital_management_backend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReceptionistRequest {
    private String name;
    private String email;
    private String password;
}

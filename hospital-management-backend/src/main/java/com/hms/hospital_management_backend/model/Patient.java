package com.hms.hospital_management_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer age;
    private String phone;
    private String address;

    @Column(length = 2000)
    private String medicalHistory;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({ "doctor", "patient", "receptionist" })
    private User user;

}

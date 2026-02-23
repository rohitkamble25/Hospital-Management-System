package com.hms.hospital_management_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specialization;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({ "doctor", "patient", "receptionist" })
    private User user;

}

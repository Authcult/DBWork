package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientID;

    @Column(nullable = false)
    private String name;

    private String gender;
    private String address;
    private String phone;
    private String username;
    private String password;
}


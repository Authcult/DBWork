package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "OutpatientRegistration")
@Data
public class OutpatientRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer registrationID;

    @ManyToOne
    @JoinColumn(name = "patientID", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctorID", nullable = false)
    private Doctor doctor;

    @Column(name="status",nullable = true)
    private String status;

    private LocalDateTime registrationTime = LocalDateTime.now();
}

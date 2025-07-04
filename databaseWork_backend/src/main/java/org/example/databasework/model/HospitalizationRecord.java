package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "HospitalizationRecord")
@Data
public class HospitalizationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordID;

    @ManyToOne
    @JoinColumn(name = "patientID", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctorID", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "wardID", nullable = false)
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "bedID", nullable = false)
    private Bed bed;

    private LocalDate admissionDate;
    private LocalDate dischargeDate;
}
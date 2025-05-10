package org.example.databasework.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prescriptionID;

    @ManyToOne
    @JoinColumn(name = "registrationID", nullable = false)
    private OutpatientRegistration registration;

    private String symptomDescription;

    @Column(precision = 10, scale = 2)
    private BigDecimal diagnosisFee;
}
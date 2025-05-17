package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "Prescription")
@Data
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

    @Column(precision = 10, scale = 2)
    private BigDecimal totalDrugFee;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;
}
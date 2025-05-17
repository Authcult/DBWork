package org.example.databasework.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentID;

    @ManyToOne
    @JoinColumn(name = "patientID", nullable = false)
    private Patient patient;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private String paymentType;

    private LocalDateTime paymentDate = LocalDateTime.now();
}
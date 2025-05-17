package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PrescriptionItem")
@Data
public class PrescriptionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemID;

    @ManyToOne
    @JoinColumn(name = "prescriptionID", nullable = false)
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(name = "drugID", nullable = false)
    private Drug drug;

    @Column(nullable = false)
    private Integer quantity;

    private String usageInstruction;
}


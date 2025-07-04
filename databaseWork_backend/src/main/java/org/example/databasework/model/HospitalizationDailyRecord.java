package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "HospitalizationDailyRecord")
@Data
public class HospitalizationDailyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dailyRecordID;

    @ManyToOne
    @JoinColumn(name = "recordID", nullable = false)
    private HospitalizationRecord record;

    private LocalDate date = LocalDate.now();

    private String treatmentPlan;
}

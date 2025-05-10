package org.example.databasework.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "HospitalizationDailyRecord")
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

package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Schedule")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleID;

    @ManyToOne
    @JoinColumn(name = "doctorID", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private String workType;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
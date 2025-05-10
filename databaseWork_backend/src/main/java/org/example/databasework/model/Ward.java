package org.example.databasework.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Ward")
public class Ward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wardID;

    private String location;

    @Column(precision = 10, scale = 2)
    private BigDecimal chargeStandard;

    @ManyToOne
    @JoinColumn(name = "departmentID", nullable = false)
    private Department department;
}

package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Ward")
public class Ward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wardID;

    private String location;

    @Column(name = "chargeStandard",precision = 10, scale = 2)
    private BigDecimal chargeStandard;

    @ManyToOne
    @JoinColumn(name = "departmentID", nullable = false)
    private Department department;
}

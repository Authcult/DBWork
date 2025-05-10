package org.example.databasework.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Bed")
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bedID;

    @Column(nullable = false)
    private String bedNumber;

    @ManyToOne
    @JoinColumn(name = "wardID", nullable = false)
    private Ward ward;
}

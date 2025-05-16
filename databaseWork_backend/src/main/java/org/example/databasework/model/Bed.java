package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
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

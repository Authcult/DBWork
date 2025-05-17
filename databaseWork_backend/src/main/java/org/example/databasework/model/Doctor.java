package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doctorID;

    @Column(nullable = false, length = 50)
    private String name;

    private String gender;
    private String title;
    private String phone;
    private String password;

    @ManyToOne
    @JoinColumn(name = "departmentID", nullable = false)
    private Department department;
}

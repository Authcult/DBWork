package org.example.databasework.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminID;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;
    private String phone;
    private String email;
}

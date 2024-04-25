package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
@Table
public class SpouseChildren {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "spouse_id")
    private Spouse spouse;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String childFullName;
    private String childGender;
    private LocalDate childDateOfBirth;
    private String childJob;
}

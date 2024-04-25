package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Table
public class Appreciation {
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String appreciationNumber;
    private LocalDate appreciationDate;
    private String appreciationDescription;

}

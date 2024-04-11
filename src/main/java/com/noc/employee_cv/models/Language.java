package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table
@Data
public class Language {

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "language")
    private Set<EmployeeLanguage> employeeLanguage;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String language;
}

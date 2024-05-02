package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Data
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String language;

    @ManyToMany(mappedBy = "languages")
    private Set<Employee> employees = new HashSet<>();
}

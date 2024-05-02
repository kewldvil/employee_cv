package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table
public class UniversitySkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String skill;

    @ManyToMany(mappedBy = "universitySkills")
    private Set<Employee> employees = new HashSet<>();
}

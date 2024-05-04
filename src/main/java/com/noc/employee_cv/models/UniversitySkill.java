package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UniversitySkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String skill;

    @OneToMany(mappedBy = "universitySkill", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmployeeUniversitySkill> employeeUniversitySkills = new HashSet<>();
}

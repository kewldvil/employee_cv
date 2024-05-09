package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @JsonIgnore
    @OneToMany(mappedBy = "universitySkill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeUniversitySkill> employeeUniversitySkills = new ArrayList<>();
}

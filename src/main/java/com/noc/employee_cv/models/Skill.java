package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String skillName;
    private boolean enabled;


    @JsonIgnore
    @ManyToMany(mappedBy = "skills")
    private Set<Employee> employees = new HashSet<>();
}

package com.noc.employee_cv.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.enums.EducationLevel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Table
public class DegreeLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    private Boolean isChecked;
    private String educationLevel;


    @JsonIgnore
    @OneToMany(mappedBy ="degreeLevel")
    private Set<EmployeeDegreeLevel> employeeDegreeLevels= new HashSet<>();

}

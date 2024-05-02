package com.noc.employee_cv.models;

import com.noc.employee_cv.enums.EducationLevel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Entity
@Table
public class DegreeLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private boolean degreeLevel;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}

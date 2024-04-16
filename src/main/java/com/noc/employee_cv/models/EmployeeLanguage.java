package com.noc.employee_cv.models;

import com.noc.employee_cv.enums.SkillLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "employee_language")
public class EmployeeLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "language_id")
    private Language language;
    @Enumerated
    private SkillLevel  skillLevel;

}

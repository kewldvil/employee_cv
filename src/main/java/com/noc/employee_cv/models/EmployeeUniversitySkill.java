package com.noc.employee_cv.models;

import com.noc.employee_cv.enums.AddressType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employee_university_skill")
@Data
@Setter
@Getter
public class EmployeeUniversitySkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "university_skill_id")
    private UniversitySkill universitySkill;
}

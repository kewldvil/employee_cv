package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Table
public class EmployeeParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private boolean isAlive;
    private String jobName;
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}

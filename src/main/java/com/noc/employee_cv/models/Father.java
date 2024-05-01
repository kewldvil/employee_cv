package com.noc.employee_cv.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Table
public class Father {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String fullName;
    @NotNull
    private String gender;
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    private boolean isAlive;
    @NotNull
    private String jobName;
    private String phoneNumber;
    @NotNull
    private boolean isFather=true;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}

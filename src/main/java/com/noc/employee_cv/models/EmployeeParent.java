package com.noc.employee_cv.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@NoArgsConstructor
@Table
public class EmployeeParent {
    @Id
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private boolean isAlive;
//    private Address placeOfBirth;
    private String jobName;
    private String phoneNumber;
//    private Address address;


}

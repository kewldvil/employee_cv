package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PhoneNumber {
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String phoneNumber;
}

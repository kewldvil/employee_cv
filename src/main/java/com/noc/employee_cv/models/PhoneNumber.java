package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PhoneNumber {
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "spouse_id")
    private Spouse spouse;

    @ManyToOne
    @JoinColumn(name = "father_id")
    private Father father;

    @ManyToOne
    @JoinColumn(name = "mother_id")
    private Mother mother;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String phoneNumber;
}

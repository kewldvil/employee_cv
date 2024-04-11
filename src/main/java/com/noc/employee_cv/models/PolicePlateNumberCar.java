package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PolicePlateNumberCar {
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String plateNumber;
    private String carType;
}

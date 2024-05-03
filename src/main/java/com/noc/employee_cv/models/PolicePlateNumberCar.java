package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PolicePlateNumberCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String plateNumber;
    private String carType;

    @ManyToOne()
    @JoinColumn(name="employee_id")
    private Employee employee;
}

package com.noc.employee_cv.models;

import com.noc.employee_cv.enums.PhoneType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "phone_numbers")
public class PhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String phoneNumber;
    private PhoneType phoneType;

    @ManyToOne()
    @JoinColumn(name="employee_id")
    private Employee employee;

}

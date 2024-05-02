package com.noc.employee_cv.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "phone_numbers")
@Data
public class PhoneNumber {

    @ManyToOne
    private Employee employee;

//    @ManyToOne
//    @JoinColumn(name = "spouse_id")
//    private Spouse spouse;
//
//    @ManyToOne
//    @JoinColumn(name = "father_id")
//    private Father father;
//
//    @ManyToOne
//    @JoinColumn(name = "mother_id")
//    private Mother mother;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String phoneNumber;
}

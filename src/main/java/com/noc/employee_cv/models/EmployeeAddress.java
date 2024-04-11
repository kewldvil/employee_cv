//package com.noc.employee_cv.models;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Table(name = "employee_address")
//@Data
//public class EmployeeAddress {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Integer id;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "employee_id")
//    private Employee employee;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "address_id")
//    private Address address;
//
//    private String addressType;
//
//}

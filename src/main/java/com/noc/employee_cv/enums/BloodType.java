package com.noc.employee_cv.enums;

public enum BloodType {
    O_POSITIVE("O+"),
    O_NEGATIVE("O-"),
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-");

    public final String bloodType;

    BloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}

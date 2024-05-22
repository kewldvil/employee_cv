package com.noc.employee_cv.enums;

import java.util.Arrays;

public enum BloodType implements CommonEnum{
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

    @Override
    public String getValue() {
        return bloodType;
    }

    public static String getValueByKey(String key) {
        for (PoliceRank rank : PoliceRank.values()) {
            if (rank.name().equalsIgnoreCase(key)) {
                return rank.getValue();
            }
        }
        return null; // or throw an exception, or return a default value
    }
}

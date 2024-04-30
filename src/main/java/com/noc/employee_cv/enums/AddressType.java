package com.noc.employee_cv.enums;

public enum AddressType {
    POB(1), CURRENT_ADDRESS(2),
    SPOUSE_POB(3), SPOUSE_ADDRESS(4),
    FATHER_POB(5), FATHER_ADDRESS(6),
    MOTHER_POB(7), MOTHER_ADDRESS(8);

    public final int addressTypeId;

    AddressType(int addressTypeId) {
        this.addressTypeId = addressTypeId;
    }
}

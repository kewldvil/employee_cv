package com.noc.employee_cv.enums;

public enum Education {
    PRIMARY(1, "សញ្ញាបត្របឋមភូមិ"),
    SECONDARY(2, "សញ្ញាបត្រអនុវីទ្យាល័យ"),
    HIGH_SCHOOL(3, "សញ្ញាបត្រវិទ្យាល័យ"),
    DIPLOMA(4, "សញ្ញាបត្របរិញ្ញាបត្ររង"),
    BACHELOR(5, "សញ្ញាបត្របរិញ្ញាបត្រ"),
    MASTER(6, "សញ្ញាបត្រថ្នាក់អនុបណ្ឌិត"),
    DOCTORAL(7, "សញ្ញាបត្រថ្នាក់បណ្ឌិត");

    public final int key;
    public final String value;

    Education(int key, String value) {
        this.key = key;
        this.value = value;
    }

}

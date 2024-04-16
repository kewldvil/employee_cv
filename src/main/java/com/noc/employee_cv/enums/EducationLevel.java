package com.noc.employee_cv.enums;

public enum EducationLevel {
    PRIMARY("សញ្ញាបត្របឋមភូមិ"),
    SECONDARY("សញ្ញាបត្រអនុវីទ្យាល័យ"),
    HIGH_SCHOOL("សញ្ញាបត្រវិទ្យាល័យ"),
    DIPLOMA("សញ្ញាបត្របរិញ្ញាបត្ររង"),
    BACHELOR( "សញ្ញាបត្របរិញ្ញាបត្រ"),
    MASTER( "សញ្ញាបត្រថ្នាក់អនុបណ្ឌិត"),
    DOCTORAL("សញ្ញាបត្រថ្នាក់បណ្ឌិត");

    public final String value;

    EducationLevel( String value) {
        this.value = value;
    }

}

package com.sopheak.staff_cv.enums;

public enum Position {
    DIRECTOR_OF_DEPARTMENT(1, "ប្រធាននាយកដ្ឋាន"),
    DEPUTY_DIRECTOR_OF_DEPARTMENT(2, "អនុប្រធាននាយកដ្ឋាន"),
    HEAD_OF_BUREAU(3, "នាយការិយាល័យ"),
    DEPUTY_HEAD_OF_BUREAU(4, "នាយរងការិយាល័យ"),
    HEAD_OF_SECTION(5, "នាយផ្នែក"),
    DEPUTY_HEAD_OF_SECTION(6, "នាយរងផ្នែក"),
    OFFICER(7, "មន្រ្តី");


    public final int key;
    public final String value;

    Position(int key, String value) {
        this.key = key;
        this.value = value;
    }
}

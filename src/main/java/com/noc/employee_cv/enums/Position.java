package com.noc.employee_cv.enums;

public enum Position {
    DIRECTOR_OF_DEPARTMENT("ប្រធាននាយកដ្ឋាន"),
    DEPUTY_DIRECTOR_OF_DEPARTMENT( "អនុប្រធាននាយកដ្ឋាន"),
    HEAD_OF_BUREAU( "នាយការិយាល័យ"),
    DEPUTY_HEAD_OF_BUREAU("នាយរងការិយាល័យ"),
    HEAD_OF_SECTION("នាយផ្នែក"),
    DEPUTY_HEAD_OF_SECTION("នាយរងផ្នែក"),
    OFFICER( "មន្រ្តី");



    public final String value;

    Position( String value) {

        this.value = value;
    }
}

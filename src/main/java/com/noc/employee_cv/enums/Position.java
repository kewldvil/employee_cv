package com.noc.employee_cv.enums;

public enum Position implements CommonEnum{
    DIRECTOR_OF_DEPARTMENT("ប្រធាននាយកដ្ឋាន"),
    DEPUTY_DIRECTOR_OF_DEPARTMENT( "អនុប្រធាននាយកដ្ឋាន"),
    HEAD_OF_BUREAU( "នាយការិយាល័យ"),
    DEPUTY_HEAD_OF_BUREAU("នាយរងការិយាល័យ"),
    HEAD_OF_SECTION("នាយផ្នែក"),
    DEPUTY_HEAD_OF_SECTION("នាយរងផ្នែក"),
    OFFICER( "មន្រ្តី"),
    TRAINEE("មន្រ្តីហាត់ការ"),
    OTHER("ផ្សេងៗ");



    public final String value;

    Position( String value) {

        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}

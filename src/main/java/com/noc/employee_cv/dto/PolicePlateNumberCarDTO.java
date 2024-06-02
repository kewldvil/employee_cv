package com.noc.employee_cv.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.noc.employee_cv.models.Employee;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class PolicePlateNumberCarDTO {
    private Integer id;
    private String vehicleBrand;
    private String vehicleNumber;

}

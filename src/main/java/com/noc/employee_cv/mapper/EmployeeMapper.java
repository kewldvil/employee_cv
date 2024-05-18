package com.noc.employee_cv.mapper;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.models.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee fromEmployeeDto(EmployeeDTO employeeDTO);

}
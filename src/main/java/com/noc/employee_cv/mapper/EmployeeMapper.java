package com.noc.employee_cv.mapper;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.models.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee fromEmployeeDto(EmployeeDTO employeeDTO);
    EmployeeDTO toEmployeeDto(Employee employee);

    void fromEmployeeDtoPartially(EmployeeDTO employeeDTO, @MappingTarget Employee employee);

}

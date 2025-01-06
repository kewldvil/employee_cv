package com.noc.employee_cv.mapper;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.models.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;



@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    // Map DTO to Employee
    @Mapping(source = "currentPositionId", target = "currentPosition.id")
    @Mapping(source = "previousPositionId", target = "previousPosition.id")
    Employee fromEmployeeDto(EmployeeDTO employeeDTO);

    // Map Employee to DTO
    @Mapping(target = "currentPositionId", source = "currentPosition.id")
    @Mapping(target = "previousPositionId", source = "previousPosition.id")
    EmployeeDTO toEmployeeDto(Employee employee);

    void updateEmployeeFromDto(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}

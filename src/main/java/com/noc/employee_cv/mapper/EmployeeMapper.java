package com.noc.employee_cv.mapper;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;



@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    // Map DTO to Employee
    @Mapping(target = "currentPosition", ignore = true)
    @Mapping(target = "previousPosition", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "placeOfBirth", ignore = true)
    @Mapping(target = "currentAddress", ignore = true)
    @Mapping(target = "spouse", ignore = true)
    @Mapping(target = "father", ignore = true)
    @Mapping(target = "mother", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "bureau", ignore = true)
    @Mapping(target = "policePlateNumberCars", ignore = true)
    @Mapping(target = "weapons", ignore = true)
    @Mapping(target = "employeeDegreeLevels", ignore = true)
    @Mapping(target = "appreciations", ignore = true)
    @Mapping(target = "vocationalTrainings", ignore = true)
    @Mapping(target = "activityAndPositions", ignore = true)
    @Mapping(target = "employeeLanguages", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "fileNames", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Employee fromEmployeeDto(EmployeeDTO employeeDTO);

    // Map Employee to DTO
    @Mapping(target = "currentPositionId", source = "currentPosition.id")
    @Mapping(target = "previousPositionId", source = "previousPosition.id")
    EmployeeDTO toEmployeeDto(Employee employee);

    @Mapping(target = "currentPosition", ignore = true)
    @Mapping(target = "previousPosition", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "placeOfBirth", ignore = true)
    @Mapping(target = "currentAddress", ignore = true)
    @Mapping(target = "spouse", ignore = true)
    @Mapping(target = "father", ignore = true)
    @Mapping(target = "mother", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "bureau", ignore = true)
    @Mapping(target = "policePlateNumberCars", ignore = true)
    @Mapping(target = "weapons", ignore = true)
    @Mapping(target = "employeeDegreeLevels", ignore = true)
    @Mapping(target = "appreciations", ignore = true)
    @Mapping(target = "vocationalTrainings", ignore = true)
    @Mapping(target = "activityAndPositions", ignore = true)
    @Mapping(target = "employeeLanguages", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "fileNames", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEmployeeFromDto(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}

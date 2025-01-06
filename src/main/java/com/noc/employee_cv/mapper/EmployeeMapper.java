package com.noc.employee_cv.mapper;

import com.noc.employee_cv.dto.*;
import com.noc.employee_cv.models.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;



@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    // Map DTO to Employee
    @Mapping(source = "currentPositionId", target = "currentPosition.id")
    @Mapping(source = "previousPositionId", target = "previousPosition.id")
    @Mapping(source = "placeOfBirth", target = "placeOfBirth")
    @Mapping(source = "currentAddress", target = "currentAddress")
    @Mapping(source = "policePlateNumberCars", target = "policePlateNumberCars")
    @Mapping(source = "weapons", target = "weapons")
    @Mapping(source = "degreeLevels", target = "employeeDegreeLevels")
    @Mapping(source = "vocationalTrainings", target = "vocationalTrainings")
    @Mapping(source = "appreciations", target = "appreciations")
    @Mapping(source = "activityAndPositions", target = "activityAndPositions")
    @Mapping(source = "spouse", target = "spouse")
    @Mapping(source = "father", target = "father")
    @Mapping(source = "mother", target = "mother")
    Employee mapFromEmployeeDto(EmployeeDTO employeeDTO);

    // Map AddressDTO to Address
    Address mapToAddress(AddressDTO addressDTO);

    // Map PolicePlateNumberCarDTO to PolicePlateNumberCar
    PolicePlateNumberCar mapToPolicePlateNumberCar(PolicePlateNumberCarDTO carDTO);

    // Map WeaponDTO to Weapon
    Weapon mapToWeapon(WeaponDTO weaponDTO);

    // Map EmployeeDegreeLevelDTO to EmployeeDegreeLevel
    EmployeeDegreeLevel mapToEmployeeDegreeLevel(EmployeeDegreeLevelDTO degreeLevelDTO);

    // Map VocationalTrainingDTO to VocationalTraining
    VocationalTraining mapToVocationalTraining(VocationalTrainingDTO trainingDTO);

    // Map AppreciationDTO to Appreciation
    Appreciation mapToAppreciation(AppreciationDTO appreciationDTO);

    // Map PreviousActivityAndPositionDTO to PreviousActivityAndPosition
    PreviousActivityAndPosition mapToPreviousActivityAndPosition(PreviousActivityAndPositionDTO previousActivityAndPositionDTO);

    // Map SpouseDTO to Spouse
    Spouse mapToSpouse(SpouseDTO spouseDTO);

    // Map ParentDTO to Father
    Father mapToFather(ParentDTO parentDTO);

    // Map ParentDTO to Mother
    Mother mapToMother(ParentDTO parentDTO);

    // Map Employee to DTO
    @Mapping(source = "placeOfBirth", target = "placeOfBirth")
    @Mapping(source = "currentAddress", target = "currentAddress")
    @Mapping(source = "policePlateNumberCars", target = "policePlateNumberCars")
    @Mapping(source = "weapons", target = "weapons")
    @Mapping(source = "employeeDegreeLevels", target = "degreeLevels")
    @Mapping(source = "vocationalTrainings", target = "vocationalTrainings")
    @Mapping(source = "appreciations", target = "appreciations")
    @Mapping(source = "activityAndPositions", target = "activityAndPositions")
    @Mapping(source = "spouse", target = "spouse")
    @Mapping(source = "father", target = "father")
    @Mapping(source = "mother", target = "mother")
    EmployeeDTO mapToEmployeeDTO(Employee employee);

    AddressDTO mapToAddressDTO(Address address);

    PolicePlateNumberCarDTO mapToPolicePlateNumberCarDTO(PolicePlateNumberCar car);

    WeaponDTO mapToWeaponDTO(Weapon weapon);

    EmployeeDegreeLevelDTO mapToEmployeeDegreeLevelDTO(EmployeeDegreeLevel degreeLevel);

    VocationalTrainingDTO mapToVocationalTrainingDTO(VocationalTraining training);

    AppreciationDTO mapToAppreciationDTO(Appreciation appreciation);

    PreviousActivityAndPositionDTO mapToPreviousActivityAndPositionDTO (PreviousActivityAndPosition previousActivityAndPosition);

    SpouseDTO mapToSpouseDTO(Spouse spouse);

    ParentDTO mapToParentDTO(Father father);
    ParentDTO mapToParentDTO(Mother father);

    // Update Employee from DTO
    @Mapping(source = "currentPositionId", target = "currentPosition.id")
    @Mapping(source = "previousPositionId", target = "previousPosition.id")
    @Mapping(source = "placeOfBirth", target = "placeOfBirth")
    @Mapping(source = "currentAddress", target = "currentAddress")
    @Mapping(source = "policePlateNumberCars", target = "policePlateNumberCars")
    @Mapping(source = "weapons", target = "weapons")
    @Mapping(source = "degreeLevels", target = "employeeDegreeLevels")
    @Mapping(source = "vocationalTrainings", target = "vocationalTrainings")
    @Mapping(source = "appreciations", target = "appreciations")
    @Mapping(source = "activityAndPositions", target = "activityAndPositions")
    @Mapping(source = "spouse", target = "spouse")
    @Mapping(source = "father", target = "father")
    @Mapping(source = "mother", target = "mother")
    void updateEmployeeFromDto(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}

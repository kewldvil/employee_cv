package com.noc.employee_cv.mapper;

import com.noc.employee_cv.dto.*;
import com.noc.employee_cv.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;



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

    default EmployeeDTO toEmployeeDetailDto(Employee employee, List<UserFileDTO> fileNames) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstname(employee.getFirstname());
        dto.setLastname(employee.getLastname());
        dto.setLatinName(employee.getLatinName());
        dto.setNationality(employee.getNationality());
        dto.setGender(employee.getGender());
        dto.setPoliceId(employee.getPoliceId());
        dto.setIsMarried(employee.getIsMarried());
        dto.setBloodType(employee.getBloodType());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setPhoneNumber(employee.getPhoneNumber());
        dto.setCurrentPoliceRank(employee.getCurrentPoliceRank());
        dto.setCurrentPositionId(id(employee.getCurrentPosition()));
        dto.setPoliceRankDocumentNumber(employee.getPoliceRankDocumentNumber());
        dto.setPositionDocumentNumber(employee.getPositionDocumentNumber());
        dto.setDepartmentId(id(employee.getDepartment()));
        dto.setPoliceRankDocumentIssueDate(employee.getPoliceRankDocumentIssueDate());
        dto.setPositionDocumentIssueDate(employee.getPositionDocumentIssueDate());
        dto.setDateJoinGov(employee.getDateJoinGov());
        dto.setDateJoinPolice(employee.getDateJoinPolice());
        dto.setPrevPoliceRank(employee.getPrevPoliceRank());
        dto.setPreviousPositionId(id(employee.getPreviousPosition()));
        dto.setGeneralDepartment(employee.getGeneralDepartment());
        dto.setPreviousActivityAndPositionStartYear(employee.getPreviousActivityAndPositionStartYear());
        dto.setPlaceOfBirth(toAddressDto(employee.getPlaceOfBirth()));
        dto.setCurrentAddress(toAddressDto(employee.getCurrentAddress()));
        dto.setSpouse(toSpouseDto(employee.getSpouse()));
        dto.setFather(toParentDto(employee.getFather()));
        dto.setMother(toParentDto(employee.getMother()));
        dto.setUserId(employee.getUser() == null ? null : employee.getUser().getId());
        dto.setPolicePlateNumberCars(employee.getPolicePlateNumberCars().stream().map(this::toPoliceCarDto).toList());
        dto.setWeapons(employee.getWeapons().stream().map(this::toWeaponDto).toList());
        dto.setDegreeLevels(employee.getEmployeeDegreeLevels().stream().map(this::toDegreeLevelDto).toList());
        dto.setAppreciations(employee.getAppreciations().stream()
                .sorted(Comparator.comparing(Appreciation::getAppreciationDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::toAppreciationDto)
                .toList());
        dto.setVocationalTrainings(employee.getVocationalTrainings().stream()
                .sorted(Comparator.comparing(VocationalTraining::getTrainingStartDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::toVocationalTrainingDto)
                .toList());
        dto.setActivityAndPositions(employee.getActivityAndPositions().stream()
                .sorted(Comparator.comparing(PreviousActivityAndPosition::getFromDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::toPreviousActivityAndPositionDto)
                .toList());
        dto.setEmployeeLanguages(employee.getEmployeeLanguages().stream().map(this::toEmployeeLanguageDto).toList());
        dto.setEmployeeSkills(employee.getSkills().stream().map(this::toEmployeeSkillDto).toList());
        dto.setFileNames(fileNames);
        return dto;
    }

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

    private int id(Position position) {
        return position == null || position.getId() == null ? 0 : position.getId();
    }

    private int id(Department department) {
        return department == null || department.getId() == null ? 0 : department.getId();
    }

    private AddressDTO toAddressDto(Address address) {
        if (address == null) {
            return null;
        }
        AddressDTO dto = new AddressDTO();
        dto.setProvince(address.getProvinces().stream().filter(Objects::nonNull).findFirst().map(ProvinceCity::getId).orElse(0));
        dto.setDistrict(address.getDistricts().stream().filter(Objects::nonNull).findFirst().map(District::getId).orElse(0));
        dto.setCommune(address.getCommunes().stream().filter(Objects::nonNull).findFirst().map(Commune::getId).orElse(0));
        dto.setVillage(address.getVillages().stream().filter(Objects::nonNull).findFirst().map(Village::getId).orElse(0));
        dto.setStreetNumber(address.getStreetNumber());
        dto.setHouseNumber(address.getHouseNumber());
        return dto;
    }

    private SpouseDTO toSpouseDto(Spouse spouse) {
        if (spouse == null) {
            return null;
        }
        SpouseDTO dto = new SpouseDTO();
        dto.setId(spouse.getId());
        dto.setFullName(spouse.getFullName());
        dto.setDateOfBirth(spouse.getDateOfBirth());
        dto.setJob(spouse.getJob());
        dto.setIsAlive(spouse.getIsAlive());
        dto.setPhoneNumber(spouse.getPhoneNumber());
        dto.setPlaceOfBirth(toAddressDto(spouse.getPlaceOfBirth()));
        dto.setCurrentAddress(toAddressDto(spouse.getCurrentAddress()));
        dto.setChildren(spouse.getChildren().stream()
                .sorted(Comparator.comparing(SpouseChildren::getDateOfBirth, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(this::toChildDto)
                .toList());
        return dto;
    }

    private ParentDTO toParentDto(Father parent) {
        if (parent == null) {
            return null;
        }
        ParentDTO dto = new ParentDTO();
        dto.setId(parent.getId());
        dto.setFullName(parent.getFullName());
        dto.setDateOfBirth(parent.getDateOfBirth());
        dto.setJob(parent.getJob());
        dto.setIsAlive(parent.getIsAlive());
        dto.setPhoneNumber(parent.getPhoneNumber());
        dto.setPlaceOfBirth(toAddressDto(parent.getPlaceOfBirth()));
        dto.setCurrentAddress(toAddressDto(parent.getCurrentAddress()));
        return dto;
    }

    private ParentDTO toParentDto(Mother parent) {
        if (parent == null) {
            return null;
        }
        ParentDTO dto = new ParentDTO();
        dto.setId(parent.getId());
        dto.setFullName(parent.getFullName());
        dto.setDateOfBirth(parent.getDateOfBirth());
        dto.setJob(parent.getJob());
        dto.setIsAlive(parent.getIsAlive());
        dto.setPhoneNumber(parent.getPhoneNumber());
        dto.setPlaceOfBirth(toAddressDto(parent.getPlaceOfBirth()));
        dto.setCurrentAddress(toAddressDto(parent.getCurrentAddress()));
        return dto;
    }

    private ChildDTO toChildDto(SpouseChildren child) {
        ChildDTO dto = new ChildDTO();
        dto.setId(child.getId());
        dto.setFullName(child.getFullName());
        dto.setGender(child.getGender());
        dto.setDateOfBirth(child.getDateOfBirth());
        dto.setJob(child.getJob());
        return dto;
    }

    private PolicePlateNumberCarDTO toPoliceCarDto(PolicePlateNumberCar car) {
        PolicePlateNumberCarDTO dto = new PolicePlateNumberCarDTO();
        dto.setId(car.getId());
        dto.setVehicleBrand(car.getVehicleBrand());
        dto.setVehicleNumber(car.getVehicleNumber());
        return dto;
    }

    private WeaponDTO toWeaponDto(Weapon weapon) {
        WeaponDTO dto = new WeaponDTO();
        dto.setId(weapon.getId());
        dto.setWeaponType(weapon.getWeaponType());
        dto.setWeaponBrand(weapon.getWeaponBrand());
        dto.setWeaponSerialNumber(weapon.getWeaponSerialNumber());
        return dto;
    }

    private EmployeeDegreeLevelDTO toDegreeLevelDto(EmployeeDegreeLevel degreeLevel) {
        EmployeeDegreeLevelDTO dto = new EmployeeDegreeLevelDTO();
        dto.setId(degreeLevel.getId());
        dto.setIsChecked(degreeLevel.getIsChecked());
        dto.setEducationLevel(degreeLevel.getDegreeLevel() == null ? null : degreeLevel.getDegreeLevel().getEducationLevel());
        return dto;
    }

    private AppreciationDTO toAppreciationDto(Appreciation appreciation) {
        AppreciationDTO dto = new AppreciationDTO();
        dto.setId(appreciation.getId());
        dto.setAppreciationNumber(appreciation.getAppreciationNumber());
        dto.setAppreciationDate(appreciation.getAppreciationDate());
        dto.setAppreciation(appreciation.getAppreciation());
        return dto;
    }

    private VocationalTrainingDTO toVocationalTrainingDto(VocationalTraining training) {
        VocationalTrainingDTO dto = new VocationalTrainingDTO();
        dto.setId(training.getId());
        dto.setTrainingCenter(training.getTrainingCenter());
        dto.setTrainingCourse(training.getTrainingCourse());
        dto.setTrainingDuration(training.getTrainingDuration());
        dto.setIsNoStartDayMonth(training.getIsNoStartDayMonth());
        dto.setIsNoEndDayMonth(training.getIsNoEndDayMonth());
        dto.setTrainingStartDate(training.getTrainingStartDate());
        dto.setTrainingToDate(training.getTrainingToDate());
        return dto;
    }

    private PreviousActivityAndPositionDTO toPreviousActivityAndPositionDto(PreviousActivityAndPosition activity) {
        PreviousActivityAndPositionDTO dto = new PreviousActivityAndPositionDTO();
        dto.setId(activity.getId());
        dto.setFromDate(activity.getFromDate());
        dto.setToDate(activity.getToDate());
        dto.setActivityAndRank(activity.getActivityAndRank());
        dto.setDepartmentOrUnit(activity.getDepartmentOrUnit());
        dto.setIsNoStartDayMonth(activity.getIsNoStartDayMonth());
        dto.setIsNoEndDayMonth(activity.getIsNoEndDayMonth());
        return dto;
    }

    private EmployeeLanguageDTO toEmployeeLanguageDto(EmployeeLanguage employeeLanguage) {
        EmployeeLanguageDTO dto = new EmployeeLanguageDTO();
        dto.setId(employeeLanguage.getId());
        dto.setLevel(employeeLanguage.getLevel());
        dto.setLanguageName(employeeLanguage.getLanguage() == null ? null : employeeLanguage.getLanguage().getLanguage());
        return dto;
    }

    private EmployeeSkillDTO toEmployeeSkillDto(Skill skill) {
        EmployeeSkillDTO dto = new EmployeeSkillDTO();
        dto.setId(skill.getId());
        dto.setSkillName(skill.getSkillName());
        dto.setEnabled(skill.isEnabled());
        return dto;
    }
}

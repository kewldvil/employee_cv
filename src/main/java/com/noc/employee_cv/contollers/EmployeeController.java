package com.noc.employee_cv.contollers;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.enums.*;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.repositories.AppreciationRepo;
import com.noc.employee_cv.services.serviceImp.EmployeeServiceImp;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImp service;
    private final AppreciationRepo appreciationRepo;

    @PostMapping("/")
    public void createNew(@RequestBody @Valid EmployeeDTO req) {
        service.createNew(req);
    }


    @Transactional
    //@PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Employee> saveEmployee(
            @RequestBody @Valid EmployeeDTO req
    ) throws MessagingException {
        Employee res = new Employee();

        res.setBloodType(BloodType.values()[req.getBloodTypeId()]);
        res.setCurrentPoliceRank(PoliceRank.values()[req.getCurrentPoliceRankId()]);
        res.setCurrentPosition(Position.values()[req.getCurrentPositionId()]);
        res.setDateJoinGovernmentJob(req.getDateJoinGov());
        res.setDateJoinPoliceJob(req.getDateJoinPolice());
        res.setDateOfBirth(req.getDateOfBirth());
        res.setFirstname(req.getFirstname());
        res.setGender(req.getGender());
        res.setMarried(req.getIsMarried());
        res.setLastname(req.getLastname());
        res.setGeneralDepartment(GeneralDepartment.values()[req.getDepartmentId()]);
        res.setPoliceRankDocumentIssueDate(req.getPoliceRankDocumentIssueDate());
        res.setPoliceRankDocumentNumber(req.getPoliceRankDocumentNumber());
        res.setPositionDocumentIssueDate(req.getPositionDocumentIssueDate());
        res.setPositionDocumentNumber(req.getPositionDocumentNumber());
        res.setPreviousActivityAndPositionStartYear(req.getPreviousActivityAndPositionStartYear());
        res.setPreviousPoliceRank(PoliceRank.values()[req.getPrevPoliceRankId()]);
        res.setPreviousPosition(Position.values()[req.getPrevPositionId()]);

        service.save(res);

        //set phone number
        Set<PhoneNumber> phoneNumbers = new HashSet<PhoneNumber>();
        req.getPhoneNumberList().forEach((e) -> {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setPhoneNumber(e.getPhoneNumber());
            phoneNumbers.add(phoneNumber);
        });

        res.setPhoneNumberList(phoneNumbers);

//        res.setPhoneNumberList(phoneNumberlist);
//        set police car

//        Set<PolicePlateNumberCar> policePlateNumberCarsList = new HashSet<>();
//        req.getPoliceCarList().forEach((e) -> {
//            PolicePlateNumberCar policeCar = new PolicePlateNumberCar();
//            policeCar.setCarType(e.getVehicleBrand());
//            policeCar.setPlateNumber(e.getVehicleNumber());
//            policePlateNumberCarsList.add(policeCar);
//        });
//        res.setPolicePlatNumberCars(policePlateNumberCarsList);
//// set weapon
//        Set<Weapon> weaponList = new HashSet<>();
//        req.getWeaponList().forEach((e) -> {
//            Weapon weapon = new Weapon();
//            weapon.setWeaponType(e.getWeaponType());
//            weapon.setWeaponBrand(e.getWeaponName());
//            weapon.setWeaponSerialNumber(e.getWeaponNumber());
//            weaponList.add(weapon);
//        });
//        res.setEmployeeWeapon(weaponList);
//        //        push education level to list
//        Set<DegreeLevel> degreeLevelList = new HashSet<>();
//        req.getEducationList().forEach((e) -> {
//            DegreeLevel degreeLevel = new DegreeLevel();
//            degreeLevel.setDegreeLevel(e);
//            degreeLevelList.add(degreeLevel);
//        });
//        res.setDegreeLevels(degreeLevelList);
//// set apreciation
//        Set<Appreciation> appreciationList = new HashSet<>();
//        req.getAppreciationList().forEach((e) -> {
//            Appreciation appreciation = new Appreciation();
//            appreciation.setAppreciationNumber(e.getAppreNumber());
//            appreciation.setAppreciationDate(e.getAppreDate());
//            appreciation.setAppreciationDescription(e.getAppreciation());
//            appreciationList.add(appreciation);
//        });
//        res.setAppreciation(appreciationList);
////        set previous activity and position
//        Set<PreviousActivityAndPosition> previousActivityAndPositionList = new HashSet<>();
//        req.getActivityList().forEach((e) -> {
//            PreviousActivityAndPosition previousActivityAndPosition = new PreviousActivityAndPosition();
//            previousActivityAndPosition.setFromDateToDateActivity(e.getFromDateToDate());
//            previousActivityAndPosition.setActivityAndAchievement(e.getActivityAndRank());
//            previousActivityAndPosition.setDepartment(e.getDepartmentOrUnit());
//            previousActivityAndPositionList.add(previousActivityAndPosition);
//        });
//        res.setActivityAndPosition(previousActivityAndPositionList);
////        set spouse
//        Spouse s = new Spouse();
//        s.setSpouseFullName(req.getSpouse().getFullName());
//        s.setAlive(req.getSpouse().isAlive());
//        s.setSpouseDateOfBirth(req.getSpouse().getDateOfBirth());
//        s.setSpouseJobName(req.getSpouse().getJob());
//        //set phone number
//        List<PhoneNumber> spousePhone = new ArrayList<>();
//        req.getSpouse().getPhoneNumberList().forEach((e) -> {
//            PhoneNumber phoneNumber = new PhoneNumber();
//            phoneNumber.setPhoneNumber(e.getPhoneNumber());
//            spousePhone.add(phoneNumber);
//        });
//        s.setPhoneNumberList(spousePhone);
//        //  set children
//        Set<SpouseChildren> spouseChildren = new HashSet<>();
//        req.getSpouse().getChildrenList().forEach((e) -> {
//            SpouseChildren child = new SpouseChildren();
//            child.setChildFullName(e.getFullName());
//            child.setChildGender(Gender.values()[e.getGender()]);
//            child.setChildDateOfBirth(e.getDateOfBirth());
//            child.setChildJob(e.getJob());
//        });
//        s.setChildren(spouseChildren);
//        res.setSpouse(s);
//// set father
//        if (res.getFather() != null) {
//            Father father = new Father();
//            father.setFullName(res.getFather().getFullName());
//            father.setDateOfBirth(res.getFather().getDateOfBirth());
//            father.setAlive(res.getFather().isAlive());
//            father.setJobName(res.getFather().getJobName());
//            // set phone number
//            Set<PhoneNumber> fatherPhone = new HashSet<>();
//            req.getFather().getPhoneNumberList().forEach((e) -> {
//                PhoneNumber phoneNumber = new PhoneNumber();
//                phoneNumber.setPhoneNumber(e.getPhoneNumber());
//                spousePhone.add(phoneNumber);
//            });
//            father.setPhoneNumberList(fatherPhone);
//            res.setFather(father);
//        }
//// set mother
//        if (res.getFather() != null) {
//            Mother mother = new Mother();
//            mother.setFullName(res.getMother().getFullName());
//            mother.setDateOfBirth(res.getMother().getDateOfBirth());
//            mother.setAlive(res.getMother().isAlive());
//            mother.setJobName(res.getMother().getJobName());
//            // set phone number
//            Set<PhoneNumber> motherPhone = new HashSet<>();
//            req.getMother().getPhoneNumberList().forEach((e) -> {
//                PhoneNumber phoneNumber = new PhoneNumber();
//                phoneNumber.setPhoneNumber(e.getPhoneNumber());
//                spousePhone.add(phoneNumber);
//            });
//            mother.setPhoneNumberList(motherPhone);
//            res.setMother(mother);
//        }
        System.out.println(res.toString());
//        service.save(res);
        return ResponseEntity.accepted().build();
    }
}


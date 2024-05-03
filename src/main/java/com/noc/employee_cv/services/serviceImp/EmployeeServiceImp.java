package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.dto.PhoneNumberDTO;
import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.enums.PhoneType;
import com.noc.employee_cv.mapper.EmployeeMapper;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.repositories.AddressRepo;
import com.noc.employee_cv.repositories.EmployeeAddressRepo;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.repositories.PhoneNumberRepo;
import com.noc.employee_cv.services.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final PhoneNumberRepo phoneNumberRepo;
    private final AddressRepo addressRepo;
    private final EmployeeAddressRepo employeeAddressRepo;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.fromEmployeeDto(employeeDTO);
//  phone number
        Set<PhoneNumber> phoneNumberList = new HashSet<PhoneNumber>();
        employeeDTO.getPhoneNumberList().forEach(phone -> {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setPhoneNumber(phone.getPhoneNumber());
            phoneNumber.setEmployee(employee);
            phoneNumber.setPhoneType(PhoneType.EMPLOYEE);
            employee.getPhoneNumberList().add(phoneNumber);
            phoneNumberList.add(phoneNumber);
        });
        employee.setPhoneNumberList(phoneNumberList);

// end phone number list
//        set police car

        Set<PolicePlateNumberCar> policePlateNumberCarList = new HashSet<>();
        employeeDTO.getPoliceCarList().forEach(car -> {
            PolicePlateNumberCar policeCar = new PolicePlateNumberCar();
            policeCar.setCarType(car.getVehicleBrand());
            policeCar.setPlateNumber(car.getVehicleNumber());
            policeCar.setEmployee(employee);
            employee.getPolicePlatNumberCars().add(policeCar);
            policePlateNumberCarList.add(policeCar);
        });
        employee.setPolicePlatNumberCars(policePlateNumberCarList);

//        end police car

// set weapon
        Set<Weapon> weaponList = new HashSet<>();
        employeeDTO.getWeaponList().forEach(weapon -> {
            Weapon w = new Weapon();
            w.setWeaponType(weapon.getWeaponType());
            w.setWeaponBrand(weapon.getWeaponName());
            w.setWeaponSerialNumber(weapon.getWeaponNumber());
            w.setEmployee(employee);
            employee.getWeapon().add(w);
            weaponList.add(w);
        });
        employee.setWeapon(weaponList);
        //        end weapon

        //        push education level to list
//        Set<DegreeLevel> degreeLevelList = new HashSet<>();
          List<DegreeLevel> degreeLevelList = new ArrayList<>();
        employeeDTO.getEducationList().forEach(degree -> {
            System.out.println(degree);
            DegreeLevel dl = new DegreeLevel();
            dl.setDegreeLevel(degree);
            dl.setEmployee(employee);
            employee.getDegreeLevels().add(dl);
            degreeLevelList.add(dl);
        });
        employee.setDegreeLevels(degreeLevelList);
//        end education level
// set apreciation
    Set<Appreciation> appreciationList = new HashSet<>();
        employeeDTO.getAppreciationList().forEach(edu->{
            Appreciation    app = new Appreciation();
            app.setAppreciationNumber(edu.getAppreciationNumber());
            app.setAppreciationDate(edu.getAppreciationDate());
            app.setAppreciationDescription(edu.getAppreciation());
            app.setEmployee(employee);
            employee.getAppreciation().add(app);
            appreciationList.add(app);
        });
        employee.setAppreciation(appreciationList);





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
        employeeRepo.save(employee);

    }

    @Override
    @Transactional
    public void saveEmployeeAddress(int employeeId, int addressId, AddressType addressType) {
        Employee employee = employeeRepo.findById(employeeId).orElseThrow();
        Address address = addressRepo.findById(addressId).orElseThrow();

        EmployeeAddress employeeAddress = new EmployeeAddress();
        employeeAddress.setEmployee(employee);
        employeeAddress.setAddress(address);
        employeeAddress.setAddressType(addressType);

        employee.getEmployeeAddresses().add(employeeAddress);
        address.getEmployeeAddresses().add(employeeAddress);

        employeeAddressRepo.save(employeeAddress);
    }


    @Override
    public Employee findById(Integer id) {
        return null;
    }

    @Override
    public List<Employee> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(Employee employee) {

    }


}

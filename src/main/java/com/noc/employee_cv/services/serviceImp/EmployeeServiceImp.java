package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.authentication.AuthenticationService;
import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.dto.PhoneNumberDTO;
import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.enums.Gender;
import com.noc.employee_cv.enums.PhoneType;
import com.noc.employee_cv.mapper.EmployeeMapper;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.repositories.AddressRepo;
import com.noc.employee_cv.repositories.EmployeeAddressRepo;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.repositories.PhoneNumberRepo;
import com.noc.employee_cv.security.UserDetailServiceImpl;
import com.noc.employee_cv.services.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailServiceImpl userDetailService;

    @Override
    @Transactional
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.fromEmployeeDto(employeeDTO);
        User user = (User)userDetailService.getLoggedInUser();
        employee.setUser(user);
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
        employeeDTO.getAppreciationList().forEach(edu -> {
            Appreciation app = new Appreciation();
            app.setAppreciationNumber(edu.getAppreciationNumber());
            app.setAppreciationDate(edu.getAppreciationDate());
            app.setAppreciationDescription(edu.getAppreciation());
            app.setEmployee(employee);
            employee.getAppreciation().add(app);
            appreciationList.add(app);
        });
        employee.setAppreciation(appreciationList);
//end appreciation
// set previous activity
        Set<PreviousActivityAndPosition> previousActivityAndPositionList = new HashSet<>();
        employeeDTO.getActivityList().forEach(activity -> {
            PreviousActivityAndPosition ps = new PreviousActivityAndPosition();
            ps.setFromDate(activity.getFromDate());
            ps.setToDate(activity.getToDate());
            ps.setActivityAndAchievement(activity.getActivityAndRank());
            ps.setDepartment(activity.getDepartmentOrUnit());
            ps.setEmployee(employee);
            employee.getActivityAndPosition().add(ps);
            previousActivityAndPositionList.add(ps);
        });
        employee.setActivityAndPosition(previousActivityAndPositionList);
//        end previous activity
// set spouse
        Spouse sp = employee.getSpouse();
        sp.setEmployee(employee);
//        sp.setSpouseFullName(employeeDTO.getSpouse().getFullName());
//        sp.setAlive(employeeDTO.getSpouse().isAlive());
//        sp.setSpouseDateOfBirth(employeeDTO.getSpouse().getDateOfBirth());
//        sp.setSpouseJobName(employeeDTO.getSpouse().getJob());
//        set children
        Set<SpouseChildren> spChildrenList = new HashSet<>();
        employeeDTO.getSpouse().getChildrenList().forEach(child -> {
            SpouseChildren spChild = new SpouseChildren();
            spChild.setChildFullName(child.getFullName());
            spChild.setChildGender(Gender.values()[child.getGender()]);
            spChild.setChildDateOfBirth(child.getDateOfBirth());
            spChild.setChildJob(child.getJob());
            spChild.setSpouse(sp);
            sp.getChildren().add(spChild);
            spChildrenList.add(spChild);
        });
        sp.setChildren(spChildrenList);
//        set phone number
//        Set<PhoneNumber> spPhoneList= new HashSet<>();
        employeeDTO.getSpouse().getPhoneNumberList().forEach(phone -> {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setPhoneNumber(phone.getPhoneNumber());
            phoneNumber.setPhoneType(PhoneType.SPOUSE);
            phoneNumber.setEmployee(employee);
            employee.getPhoneNumberList().add(phoneNumber);
            phoneNumberList.add(phoneNumber);
        });
        employee.setPhoneNumberList(phoneNumberList);
//end spouse and child
//        set father
        Father fa = employee.getFather();
        fa.setEmployee(employee);
        //set father's phone number
        employeeDTO.getFather().getPhoneNumberList().forEach(phone -> {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setPhoneNumber(phone.getPhoneNumber());
            phoneNumber.setPhoneType(PhoneType.FATHER);
            phoneNumber.setEmployee(employee);
            employee.getPhoneNumberList().add(phoneNumber);
            phoneNumberList.add(phoneNumber);
        });
        employee.setPhoneNumberList(phoneNumberList);
//end father
        //        set mother
        Mother mo = employee.getMother();
        mo.setEmployee(employee);
        //set mother's phone number
        employeeDTO.getMother().getPhoneNumberList().forEach(phone -> {
            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setPhoneNumber(phone.getPhoneNumber());
            phoneNumber.setPhoneType(PhoneType.MOTHER);
            phoneNumber.setEmployee(employee);
            employee.getPhoneNumberList().add(phoneNumber);
            phoneNumberList.add(phoneNumber);
        });
        employee.setPhoneNumberList(phoneNumberList);
//end mother


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

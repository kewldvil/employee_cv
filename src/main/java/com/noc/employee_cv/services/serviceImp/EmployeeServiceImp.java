package com.noc.employee_cv.services.serviceImp;
import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.enums.*;
import com.noc.employee_cv.mapper.EmployeeMapper;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.provinces.*;
import com.noc.employee_cv.repositories.*;
import com.noc.employee_cv.security.UserDetailServiceImpl;
import com.noc.employee_cv.services.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final PhoneNumberRepo phoneNumberRepo;
    private final AddressRepo addressRepo;
    private final EmployeeAddressRepo employeeAddressRepo;
    private final EmployeeMapper employeeMapper;
    private final UserDetailServiceImpl userDetailService;
    private final ProvinceCityServiceImp provinceServiceImp;
    private final DistrictServiceImp districtServiceImp;
    private final CommuneServiceImp communeServiceImp;
    private final VillageServiceImp villageServiceImp;
    private final AddressServiceImp addressServiceImp;
    private final UniversitySkillRepo universitySkillRepo;
    private final EmployeeUniversitySkillRepo employeeUniversitySkillRepo;
    private final UniversitySkillServiceImp universitySkillServiceImp;
    private final EmployeeUniversitySkillServiceImp employeeUniversitySkillServiceImp;
    private final LanguageServiceImp languageServiceImp;
    private final EmployeeLanguageServiceImp employeeLanguageServiceImp;

    @Override
    @Transactional
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.fromEmployeeDto(employeeDTO);
        User user = (User) userDetailService.getLoggedInUser();
        employee.setUser(user);
        employee.setBloodType(BloodType.values()[employeeDTO.getBloodTypeId()]);
        employee.setCurrentPoliceRank(PoliceRank.values()[employeeDTO.getCurrentPoliceRankId()]);
        employee.setCurrentPosition(Position.values()[employeeDTO.getCurrentPositionId()]);
        employee.setGeneralDepartment(GeneralDepartment.values()[employeeDTO.getDepartmentId()]);
        employee.setPreviousPoliceRank(PoliceRank.values()[employeeDTO.getPrevPoliceRankId()]);
        employee.setPreviousPosition(Position.values()[employeeDTO.getPrevPositionId()]);
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
// set appreciation
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
                        // set POB and Current Adr emp
        EmployeeAddress employeeAddress = new EmployeeAddress();
//        emp pob
        Address emp_pob = new Address();
        emp_pob.setProvince(employeeDTO.getPlaceOfBirth().getProvince());
        emp_pob.setDistrict(employeeDTO.getPlaceOfBirth().getDistrict());
        emp_pob.setCommune(employeeDTO.getPlaceOfBirth().getCommune());
        emp_pob.setVillage(employeeDTO.getPlaceOfBirth().getVillage());

        employeeAddress.setAddress(emp_pob);
        employeeAddress.setEmployee(employee);
        employeeAddress.setAddressType(AddressType.POB);

        employee.getEmployeeAddresses().add(employeeAddress);
        emp_pob.getEmployeeAddresses().add(employeeAddress);

        addressServiceImp.save(emp_pob);
        employeeAddressRepo.save(employeeAddress);
//        end emp pob
//        start emp current adr
        EmployeeAddress employeeAddress1 = new EmployeeAddress();
//        emp pob
        Address emp_current_adr = new Address();
        emp_current_adr.setProvince(employeeDTO.getCurrentAddress().getProvince());
        emp_current_adr.setDistrict(employeeDTO.getCurrentAddress().getDistrict());
        emp_current_adr.setCommune(employeeDTO.getCurrentAddress().getCommune());
        emp_current_adr.setVillage(employeeDTO.getCurrentAddress().getVillage());
        emp_current_adr.setStreetNumber(employeeDTO.getCurrentAddress().getStreetNumber());
        emp_current_adr.setHouseNumber(employeeDTO.getCurrentAddress().getHouseNumber());
        employeeAddress1.setAddress(emp_current_adr);
        employeeAddress1.setEmployee(employee);
        employeeAddress1.setAddressType(AddressType.CURRENT_ADDRESS);

        employee.getEmployeeAddresses().add(employeeAddress1);
        emp_current_adr.getEmployeeAddresses().add(employeeAddress1);

        addressServiceImp.save(emp_current_adr);
        employeeAddressRepo.save(employeeAddress1);
//        end emp current addr
                        //SPOUSE ADDRESS
        EmployeeAddress spuosePOB = new EmployeeAddress();

        Address sp_pob = new Address();
        sp_pob.setProvince(employeeDTO.getSpouse().getPlaceOfBirth().getProvince());
        sp_pob.setDistrict(employeeDTO.getSpouse().getPlaceOfBirth().getDistrict());
        sp_pob.setCommune(employeeDTO.getSpouse().getPlaceOfBirth().getCommune());
        sp_pob.setVillage(employeeDTO.getSpouse().getPlaceOfBirth().getVillage());

        spuosePOB.setAddress(sp_pob);
        spuosePOB.setEmployee(employee);
        spuosePOB.setAddressType(AddressType.SPOUSE_POB);

        employee.getEmployeeAddresses().add(spuosePOB);
        sp_pob.getEmployeeAddresses().add(spuosePOB);

        addressServiceImp.save(sp_pob);
        employeeAddressRepo.save(spuosePOB);

        EmployeeAddress SpouseAddress = new EmployeeAddress();

        Address sp_current_adr = new Address();
        sp_current_adr.setProvince(employeeDTO.getSpouse().getCurrentAddress().getProvince());
        sp_current_adr.setDistrict(employeeDTO.getSpouse().getCurrentAddress().getDistrict());
        sp_current_adr.setCommune(employeeDTO.getSpouse().getCurrentAddress().getCommune());
        sp_current_adr.setVillage(employeeDTO.getSpouse().getCurrentAddress().getVillage());
        sp_current_adr.setStreetNumber(employeeDTO.getSpouse().getCurrentAddress().getStreetNumber());
        sp_current_adr.setHouseNumber(employeeDTO.getSpouse().getCurrentAddress().getHouseNumber());
        SpouseAddress.setAddress(sp_current_adr);
        SpouseAddress.setEmployee(employee);
        SpouseAddress.setAddressType(AddressType.SPOUSE_ADDRESS);

        employee.getEmployeeAddresses().add(SpouseAddress);
        sp_current_adr.getEmployeeAddresses().add(SpouseAddress);

        addressServiceImp.save(sp_current_adr);
        employeeAddressRepo.save(SpouseAddress);
                        // END SPOUSE ADDRESS

        //FATHER ADDRESS
        EmployeeAddress faPOB = new EmployeeAddress();

        Address fa_pob = new Address();
        fa_pob.setProvince(employeeDTO.getFather().getPlaceOfBirth().getProvince());
        fa_pob.setDistrict(employeeDTO.getFather().getPlaceOfBirth().getDistrict());
        fa_pob.setCommune(employeeDTO.getFather().getPlaceOfBirth().getCommune());
        fa_pob.setVillage(employeeDTO.getFather().getPlaceOfBirth().getVillage());

        faPOB.setAddress(fa_pob);
        faPOB.setEmployee(employee);
        faPOB.setAddressType(AddressType.FATHER_POB);

        employee.getEmployeeAddresses().add(faPOB);
        fa_pob.getEmployeeAddresses().add(faPOB);

        addressServiceImp.save(fa_pob);
        employeeAddressRepo.save(spuosePOB);

        EmployeeAddress faAddress = new EmployeeAddress();

        Address fa_current_adr = new Address();
        fa_current_adr.setProvince(employeeDTO.getFather().getCurrentAddress().getProvince());
        fa_current_adr.setDistrict(employeeDTO.getFather().getCurrentAddress().getDistrict());
        fa_current_adr.setCommune(employeeDTO.getFather().getCurrentAddress().getCommune());
        fa_current_adr.setVillage(employeeDTO.getFather().getCurrentAddress().getVillage());
        fa_current_adr.setStreetNumber(employeeDTO.getFather().getCurrentAddress().getStreetNumber());
        fa_current_adr.setHouseNumber(employeeDTO.getFather().getCurrentAddress().getHouseNumber());
        faAddress.setAddress(fa_current_adr);
        faAddress.setEmployee(employee);
        faAddress.setAddressType(AddressType.FATHER_ADDRESS);

        employee.getEmployeeAddresses().add(faAddress);
        fa_current_adr.getEmployeeAddresses().add(faAddress);

        addressServiceImp.save(fa_current_adr);
        employeeAddressRepo.save(faAddress);
        // END FATHER ADDRESS
        //MOTHER ADDRESS
        EmployeeAddress moPOB = new EmployeeAddress();

        Address mo_pob = new Address();
        mo_pob.setProvince(employeeDTO.getMother().getPlaceOfBirth().getProvince());
        mo_pob.setDistrict(employeeDTO.getMother().getPlaceOfBirth().getDistrict());
        mo_pob.setCommune(employeeDTO.getMother().getPlaceOfBirth().getCommune());
        mo_pob.setVillage(employeeDTO.getMother().getPlaceOfBirth().getVillage());

        moPOB.setAddress(mo_pob);
        moPOB.setEmployee(employee);
        moPOB.setAddressType(AddressType.MOTHER_POB);

        employee.getEmployeeAddresses().add(moPOB);
        mo_pob.getEmployeeAddresses().add(moPOB);

        addressServiceImp.save(mo_pob);
        employeeAddressRepo.save(spuosePOB);

        EmployeeAddress moAddress = new EmployeeAddress();

        Address mo_current_adr = new Address();
        mo_current_adr.setProvince(employeeDTO.getMother().getCurrentAddress().getProvince());
        mo_current_adr.setDistrict(employeeDTO.getMother().getCurrentAddress().getDistrict());
        mo_current_adr.setCommune(employeeDTO.getMother().getCurrentAddress().getCommune());
        mo_current_adr.setVillage(employeeDTO.getMother().getCurrentAddress().getVillage());
        mo_current_adr.setStreetNumber(employeeDTO.getMother().getCurrentAddress().getStreetNumber());
        mo_current_adr.setHouseNumber(employeeDTO.getMother().getCurrentAddress().getHouseNumber());

        moAddress.setAddress(mo_current_adr);
        moAddress.setEmployee(employee);
        moAddress.setAddressType(AddressType.MOTHER_ADDRESS);

        employee.getEmployeeAddresses().add(moAddress);
        mo_current_adr.getEmployeeAddresses().add(moAddress);

        addressServiceImp.save(mo_current_adr);
        employeeAddressRepo.save(moAddress);
        // END MOTHER ADDRESS


// end Pob and current adr
                    //UNIVERISTY SKILL

        employeeDTO.getUniversityMajorList().forEach(skill->{
            EmployeeUniversitySkill empUniSkill = new EmployeeUniversitySkill();
            UniversitySkill universitySkill = new UniversitySkill();
            universitySkill.setSkill(skill.getMajorName());
            empUniSkill.setUniversitySkill(universitySkill);
            empUniSkill.setEmployee(employee);

            employee.getEmployeeUniversitySkills().add(empUniSkill);
            universitySkill.getEmployeeUniversitySkills().add(empUniSkill);
            universitySkillServiceImp.save(universitySkill);
            employeeUniversitySkillRepo.save(empUniSkill);

        });
                    //END UNIVERSITY SKILL
                    //START FOREIGN LANGUAGES
                    employeeDTO.getForeignLangList().forEach(lang->{
                        EmployeeLanguage empLang= new EmployeeLanguage();
                        Language language = new Language();
                        language.setLanguage(ForeignLang.values()[lang.getLangName()]);
                        empLang.setLanguage(language);
                        empLang.setEmployee(employee);
                        empLang.setLevel(SkillLevel.values()[lang.getLangLevel()]);
                        employee.getEmployeeLanguages().add(empLang);
                        language.getEmployeeLanguages().add(empLang);
                        languageServiceImp.save(language);
                        employeeLanguageServiceImp.save(empLang);

                    });
                    //END FOREIGN LANGUAGES


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

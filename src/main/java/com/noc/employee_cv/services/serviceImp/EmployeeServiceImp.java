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
import org.antlr.v4.runtime.misc.Array2DHashSet;
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
    private final AddressProvinceServiceImp addressProvinceServiceImp;
    private final AddressDistrictServiceImp addressDistrictServiceImp;
    private final AddressCommuneServiceImp addressCommuneServiceImp;
    private final AddressVillageServiceImp addressVillageServiceImp;

    @Override
    @Transactional
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.fromEmployeeDto(employeeDTO);
        User user = (User) userDetailService.getLoggedInUser();
        employee.setUser(user);
        employee.setBloodType(employeeDTO.getBloodTypeId());
        employee.setCurrentPoliceRank(employeeDTO.getCurrentPoliceRankId());
        employee.setCurrentPosition(employeeDTO.getCurrentPositionId());
        employee.setGeneralDepartment(employeeDTO.getDepartmentId());
        employee.setPreviousPoliceRank(employeeDTO.getPrevPoliceRankId());
        employee.setPreviousPosition(employeeDTO.getPrevPositionId());
//  phone number
        List<PhoneNumber> phoneNumberList = new ArrayList<>();
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

        List<PolicePlateNumberCar> policePlateNumberCarList = new ArrayList<>();
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
        List<Weapon> weaponList = new ArrayList<>();
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
            DegreeLevel dl = new DegreeLevel();
            dl.setDegreeLevel(degree);
            dl.setEmployee(employee);
            employee.getDegreeLevels().add(dl);
            degreeLevelList.add(dl);
        });
        employee.setDegreeLevels(degreeLevelList);
//        end education level
// set appreciation
        List<Appreciation> appreciationList = new ArrayList<>();
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
        // SET VOCATIONAL TRAINING
        List<VocationalTraining> vocationalTrainingList = new ArrayList<>();
        employeeDTO.getVocationalTrainingList().forEach(trainingDTO -> {
            VocationalTraining vt = new VocationalTraining();
            vt.setTrainingCenter(trainingDTO.getTrainingCenter());
            vt.setTrainingDate(trainingDTO.getTrainingDate());
            vt.setTrainingCourse(trainingDTO.getTrainingCourse());
            vt.setTrainingDuration(trainingDTO.getTrainingDuration());
            vt.setEmployee(employee);
            employee.getVocationalTraining().add(vt);
            vocationalTrainingList.add(vt);
        });
        employee.setVocationalTraining(vocationalTrainingList);
        //END VOCATIONAL TRAINING
// set previous activity
        List<PreviousActivityAndPosition> previousActivityAndPositionList = new ArrayList<>();
        employeeDTO.getActivityAndPosition().forEach(activity -> {
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
        List<SpouseChildren> spChildrenList = new ArrayList<>();
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
        //SET EMPLOYEE PLACE OF BIRTH
        ProvinceCity provinceCity = provinceServiceImp.getProvinceById(employeeDTO.getPlaceOfBirth().getProvince());
        District district = districtServiceImp.getDistrictById(employeeDTO.getPlaceOfBirth().getDistrict());
        Commune commune = communeServiceImp.getCommuneById(employeeDTO.getPlaceOfBirth().getCommune());
        Village village = villageServiceImp.getVillageById(employeeDTO.getPlaceOfBirth().getVillage());
        myPlaceOfBirth(employee, provinceCity, district, commune, village, AddressType.POB);
        //END EMPLOYEE PLACE OF BIRTH
        // EMPLOYEE CURRENT ADDRESS
        ProvinceCity cur_provinceCity = provinceServiceImp.getProvinceById(employeeDTO.getCurrentAddress().getProvince());
        District cur_district = districtServiceImp.getDistrictById(employeeDTO.getCurrentAddress().getDistrict());
        Commune cur_commune = communeServiceImp.getCommuneById(employeeDTO.getCurrentAddress().getCommune());
        Village cur_village = villageServiceImp.getVillageById(employeeDTO.getCurrentAddress().getVillage());
        var streetNumber = employeeDTO.getCurrentAddress().getStreetNumber();
        var houseNumber = employeeDTO.getCurrentAddress().getHouseNumber();
        myCurrentAddress(employee, cur_provinceCity, cur_district, cur_commune, cur_village, AddressType.CURRENT_ADDRESS, streetNumber, houseNumber);
        // END EMPLOYEE CURRENT ADDRESS

        //SET SPOUSE PLACE OF BIRTH
        ProvinceCity spouseProvinceCity = provinceServiceImp.getProvinceById(employeeDTO.getSpouse().getPlaceOfBirth().getProvince());
        District spouseDistrict = districtServiceImp.getDistrictById(employeeDTO.getSpouse().getPlaceOfBirth().getDistrict());
        Commune spouseCommune = communeServiceImp.getCommuneById(employeeDTO.getSpouse().getPlaceOfBirth().getCommune());
        Village spouseVillage = villageServiceImp.getVillageById(employeeDTO.getSpouse().getPlaceOfBirth().getVillage());
        myPlaceOfBirth(employee, spouseProvinceCity, spouseDistrict, spouseCommune, spouseVillage, AddressType.SPOUSE_POB);
        //END SPOUSE PLACE OF BIRTH
        // SPOUSE CURRENT ADDRESS
        ProvinceCity spouse_cur_provinceCity = provinceServiceImp.getProvinceById(employeeDTO.getSpouse().getCurrentAddress().getProvince());
        District spouse_cur_district = districtServiceImp.getDistrictById(employeeDTO.getSpouse().getCurrentAddress().getDistrict());
        Commune spouse_cur_commune = communeServiceImp.getCommuneById(employeeDTO.getSpouse().getCurrentAddress().getCommune());
        Village spouse_cur_village = villageServiceImp.getVillageById(employeeDTO.getSpouse().getCurrentAddress().getVillage());
        var spouse_streetNumber = employeeDTO.getSpouse().getCurrentAddress().getStreetNumber();
        var spouse_houseNumber = employeeDTO.getSpouse().getCurrentAddress().getHouseNumber();
        myCurrentAddress(employee, spouse_cur_provinceCity, spouse_cur_district, spouse_cur_commune, spouse_cur_village, AddressType.SPOUSE_ADDRESS, spouse_streetNumber, spouse_houseNumber);
        // END SPOUSE CURRENT ADDRESS
        //SET FATHER PLACE OF BIRTH
        ProvinceCity fatherProvinceCity = provinceServiceImp.getProvinceById(employeeDTO.getFather().getPlaceOfBirth().getProvince());
        District fatherDistrict = districtServiceImp.getDistrictById(employeeDTO.getFather().getPlaceOfBirth().getDistrict());
        Commune fatherCommune = communeServiceImp.getCommuneById(employeeDTO.getFather().getPlaceOfBirth().getCommune());
        Village fatherVillage = villageServiceImp.getVillageById(employeeDTO.getFather().getPlaceOfBirth().getVillage());
        myPlaceOfBirth(employee, fatherProvinceCity, fatherDistrict, fatherCommune, fatherVillage, AddressType.FATHER_POB);
        //END FATHER PLACE OF BIRTH
        // FATHER CURRENT ADDRESS
        ProvinceCity father_cur_provinceCity = provinceServiceImp.getProvinceById(employeeDTO.getFather().getCurrentAddress().getProvince());
        District father_cur_district = districtServiceImp.getDistrictById(employeeDTO.getFather().getCurrentAddress().getDistrict());
        Commune father_cur_commune = communeServiceImp.getCommuneById(employeeDTO.getFather().getCurrentAddress().getCommune());
        Village father_cur_village = villageServiceImp.getVillageById(employeeDTO.getFather().getCurrentAddress().getVillage());
        var father_streetNumber = employeeDTO.getFather().getCurrentAddress().getStreetNumber();
        var father_houseNumber = employeeDTO.getFather().getCurrentAddress().getHouseNumber();
        myCurrentAddress(employee, father_cur_provinceCity, father_cur_district, father_cur_commune, father_cur_village, AddressType.FATHER_ADDRESS, father_streetNumber, father_houseNumber);
        // END FATHER CURRENT ADDRESS
        //SET MOTHER PLACE OF BIRTH
        ProvinceCity motherProvinceCity = provinceServiceImp.getProvinceById(employeeDTO.getMother().getPlaceOfBirth().getProvince());
        District motherDistrict = districtServiceImp.getDistrictById(employeeDTO.getMother().getPlaceOfBirth().getDistrict());
        Commune motherCommune = communeServiceImp.getCommuneById(employeeDTO.getMother().getPlaceOfBirth().getCommune());
        Village motherVillage = villageServiceImp.getVillageById(employeeDTO.getMother().getPlaceOfBirth().getVillage());
        myPlaceOfBirth(employee, motherProvinceCity, motherDistrict, motherCommune, motherVillage, AddressType.MOTHER_POB);
        //END MOTHER PLACE OF BIRTH
        // MOTHER CURRENT ADDRESS
        ProvinceCity mother_cur_provinceCity = provinceServiceImp.getProvinceById(employeeDTO.getMother().getCurrentAddress().getProvince());
        District mother_cur_district = districtServiceImp.getDistrictById(employeeDTO.getMother().getCurrentAddress().getDistrict());
        Commune mother_cur_commune = communeServiceImp.getCommuneById(employeeDTO.getMother().getCurrentAddress().getCommune());
        Village mother_cur_village = villageServiceImp.getVillageById(employeeDTO.getMother().getCurrentAddress().getVillage());
        var mother_streetNumber = employeeDTO.getMother().getCurrentAddress().getStreetNumber();
        var mother_houseNumber = employeeDTO.getMother().getCurrentAddress().getHouseNumber();
        myCurrentAddress(employee, mother_cur_provinceCity, mother_cur_district, mother_cur_commune, mother_cur_village, AddressType.MOTHER_ADDRESS, mother_streetNumber, mother_houseNumber);
        // END MOTHER CURRENT ADDRESS


        //UNIVERISTY SKILL

        employeeDTO.getUniversityMajorList().forEach(skill -> {
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
        employeeDTO.getForeignLangList().forEach(lang -> {
            EmployeeLanguage empLang = new EmployeeLanguage();
            Language language = new Language();
            language.setLanguage(lang.getLangName());
            empLang.setLanguage(language);
            empLang.setEmployee(employee);
            empLang.setLevel(lang.getLangLevel());
            employee.getEmployeeLanguages().add(empLang);
            language.getEmployeeLanguages().add(empLang);
            languageServiceImp.save(language);
            employeeLanguageServiceImp.save(empLang);

        });
        //END FOREIGN LANGUAGES


        employeeRepo.save(employee);

    }

    private void myPlaceOfBirth(Employee employee, ProvinceCity provinceCity, District district, Commune commune, Village village, AddressType addressType) {
        AddressProvinceCity addressProvinceCity = new AddressProvinceCity();
        AddressDistrict addressDistrict = new AddressDistrict();
        AddressCommune addressCommune = new AddressCommune();
        AddressVillage addressVillage = new AddressVillage();

        Address emp_pob = new Address();
        addressProvinceCity.setProvinceCity(provinceCity);
        addressDistrict.setDistrict(district);
        addressCommune.setCommune(commune);
        addressVillage.setVillage(village);

        addressProvinceCity.setAddress(emp_pob);
        addressDistrict.setAddress(emp_pob);
        addressCommune.setAddress(emp_pob);
        addressVillage.setAddress(emp_pob);

        provinceCity.getAddressProvinceCities().add(addressProvinceCity);
        district.getAddressDistricts().add(addressDistrict);
        commune.getAddressCommunes().add(addressCommune);
        village.getAddressVillages().add(addressVillage);

        emp_pob.getAddressProvinceCities().add(addressProvinceCity);
        emp_pob.getAddressDistricts().add(addressDistrict);
        emp_pob.getAddressCommunes().add(addressCommune);
        emp_pob.getAddressVillages().add(addressVillage);

        addressProvinceCity.setAddress(emp_pob);
        addressDistrict.setAddress(emp_pob);
        addressCommune.setAddress(emp_pob);
        addressVillage.setAddress(emp_pob);
        addressProvinceServiceImp.save(addressProvinceCity);
        addressDistrictServiceImp.save(addressDistrict);
        addressCommuneServiceImp.save(addressCommune);
        addressVillageServiceImp.save(addressVillage);
        EmployeeAddress employeeAddress = new EmployeeAddress();
        employeeAddress.setAddress(emp_pob);
        employeeAddress.setEmployee(employee);
        employeeAddress.setAddressType(addressType);

        employee.getEmployeeAddresses().add(employeeAddress);
        emp_pob.getEmployeeAddresses().add(employeeAddress);

        addressServiceImp.save(emp_pob);
        employeeAddressRepo.save(employeeAddress);
    }

    private void myCurrentAddress(Employee employee, ProvinceCity provinceCity, District district, Commune commune, Village village, AddressType addressType, String streetNumber, String houseNumber) {
        AddressProvinceCity addressProvinceCity = new AddressProvinceCity();
        AddressDistrict addressDistrict = new AddressDistrict();
        AddressCommune addressCommune = new AddressCommune();
        AddressVillage addressVillage = new AddressVillage();

        Address emp_pob = new Address();
        emp_pob.setStreetNumber(streetNumber);
        emp_pob.setHouseNumber(houseNumber);
        addressProvinceCity.setProvinceCity(provinceCity);
        addressDistrict.setDistrict(district);
        addressCommune.setCommune(commune);
        addressVillage.setVillage(village);

        addressProvinceCity.setAddress(emp_pob);
        addressDistrict.setAddress(emp_pob);
        addressCommune.setAddress(emp_pob);
        addressVillage.setAddress(emp_pob);

        provinceCity.getAddressProvinceCities().add(addressProvinceCity);
        district.getAddressDistricts().add(addressDistrict);
        commune.getAddressCommunes().add(addressCommune);
        village.getAddressVillages().add(addressVillage);

        emp_pob.getAddressProvinceCities().add(addressProvinceCity);
        emp_pob.getAddressDistricts().add(addressDistrict);
        emp_pob.getAddressCommunes().add(addressCommune);
        emp_pob.getAddressVillages().add(addressVillage);


        addressProvinceCity.setAddress(emp_pob);
        addressDistrict.setAddress(emp_pob);
        addressCommune.setAddress(emp_pob);
        addressVillage.setAddress(emp_pob);
        addressProvinceServiceImp.save(addressProvinceCity);
        addressDistrictServiceImp.save(addressDistrict);
        addressCommuneServiceImp.save(addressCommune);
        addressVillageServiceImp.save(addressVillage);
        EmployeeAddress employeeAddress = new EmployeeAddress();
        employeeAddress.setAddress(emp_pob);
        employeeAddress.setEmployee(employee);
        employeeAddress.setAddressType(addressType);

        employee.getEmployeeAddresses().add(employeeAddress);
        emp_pob.getEmployeeAddresses().add(employeeAddress);

        addressServiceImp.save(emp_pob);
        employeeAddressRepo.save(employeeAddress);
    }

    @Override
    public void saveEmployeeAddress(int employeeId, int addressId, AddressType addressType) {
        return;
    }


    @Override
    public Employee findById(Integer id) {
        return employeeRepo.findById(id).orElseThrow();
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

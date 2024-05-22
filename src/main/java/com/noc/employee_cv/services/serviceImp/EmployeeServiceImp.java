package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.dto.SpouseDTO;
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
    private final UserRepo userRepo;
    private final SpouseRepo spouseRepo;
    private final FatherRepo fatherRepo;
    private final MotherRepo motherRepo;
    private final ChildRepo childRepo;

    @Override
    @Transactional
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.fromEmployeeDto(employeeDTO);
        System.out.println(employeeDTO.toString());
        User user = userRepo.findUserById(employeeDTO.getUserId());
        System.out.println(user.toString());
        employee.setUser(user);
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
            dl.setIsChecked(degree.getIsChecked());
            dl.setEducation_level(degree.getEducation_level());
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
        if (employee.getIsMarried()) {
            if (employee.getSpouse() != null) {
                Spouse sp = employee.getSpouse();
                System.out.println(sp.getId());
                sp.setEmployee(employee);
                // Update spouse children
                List<SpouseChildren> spChildrenList = new ArrayList<>();
                if (employeeDTO.getSpouse().getPhoneNumberList() != null) {
                    employeeDTO.getSpouse().getChildrenList().forEach(child -> {
                        SpouseChildren spChild = new SpouseChildren();
                        spChild.setChildFullName(child.getFullName());
                        spChild.setChildGender(child.getGender());
                        spChild.setChildDateOfBirth(child.getDateOfBirth());
                        spChild.setChildJob(child.getJob());
                        spChild.setSpouse(sp);
                        sp.getChildren().add(spChild);
                        spChildrenList.add(spChild);
                    });
                    sp.setChildren(spChildrenList);
                }
                if (employeeDTO.getSpouse().getPhoneNumberList() != null) {
                    employeeDTO.getSpouse().getPhoneNumberList().forEach(phone -> {
                        PhoneNumber phoneNumber = new PhoneNumber();
                        phoneNumber.setPhoneNumber(phone.getPhoneNumber());
                        phoneNumber.setPhoneType(PhoneType.SPOUSE);
                        phoneNumber.setEmployee(employee);
                        employee.getPhoneNumberList().add(phoneNumber);
                        phoneNumberList.add(phoneNumber);
                    });
                    employee.setPhoneNumberList(phoneNumberList);
                }
                spouseRepo.save(sp);
            }
        }
//end spouse and child
//        set father
        if (employee.getFather() != null) {
            Father fa = employee.getFather();
            fa.setEmployee(employee);
            //set father's phone number
            if (employeeDTO.getFather().getPhoneNumberList() != null) {
                employeeDTO.getFather().getPhoneNumberList().forEach(phone -> {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setPhoneNumber(phone.getPhoneNumber());
                    phoneNumber.setPhoneType(PhoneType.FATHER);
                    phoneNumber.setEmployee(employee);
                    employee.getPhoneNumberList().add(phoneNumber);
                    phoneNumberList.add(phoneNumber);
                });
                employee.setPhoneNumberList(phoneNumberList);
            }
        }
//end father
        //        set mother
        if (employee.getMother() != null) {
            Mother mo = employee.getMother();
            mo.setEmployee(employee);
            //set mother's phone number
            if (employeeDTO.getMother().getPhoneNumberList() != null) {
                employeeDTO.getMother().getPhoneNumberList().forEach(phone -> {
                    PhoneNumber phoneNumber = new PhoneNumber();
                    phoneNumber.setPhoneNumber(phone.getPhoneNumber());
                    phoneNumber.setPhoneType(PhoneType.MOTHER);
                    phoneNumber.setEmployee(employee);
                    employee.getPhoneNumberList().add(phoneNumber);
                    phoneNumberList.add(phoneNumber);
                });
                employee.setPhoneNumberList(phoneNumberList);
            }
        }

        //end mother
        //SET EMPLOYEE PLACE OF BIRTH
        placeOfBirthOrCurrentAddress(employee, employeeDTO.getPlaceOfBirth().

                getProvince(), employeeDTO.

                getPlaceOfBirth().

                getDistrict(), employeeDTO.

                getPlaceOfBirth().

                getCommune(), employeeDTO.

                getPlaceOfBirth().

                getVillage(), AddressType.POB, null, null);

        //END EMPLOYEE PLACE OF BIRTH
        // EMPLOYEE CURRENT ADDRESS
        placeOfBirthOrCurrentAddress(employee, employeeDTO.getCurrentAddress().

                getProvince(), employeeDTO.

                getCurrentAddress().

                getDistrict(), employeeDTO.

                getCurrentAddress().

                getCommune(), employeeDTO.

                getCurrentAddress().

                getVillage(), AddressType.CURRENT_ADDRESS, employeeDTO.getCurrentAddress().

                getStreetNumber(), employeeDTO.

                getCurrentAddress().

                getHouseNumber());
        // END EMPLOYEE CURRENT ADDRESS

        //SET SPOUSE PLACE OF BIRTH
        if (employeeDTO.getSpouse() != null) {
            placeOfBirthOrCurrentAddress(employee, employeeDTO.getSpouse().

                    getPlaceOfBirth().

                    getProvince(), employeeDTO.

                    getSpouse().

                    getPlaceOfBirth().

                    getDistrict(), employeeDTO.

                    getSpouse().

                    getPlaceOfBirth().

                    getCommune(), employeeDTO.

                    getSpouse().

                    getPlaceOfBirth().

                    getVillage(), AddressType.SPOUSE_POB, null, null);

            //END SPOUSE PLACE OF BIRTH
            // SPOUSE CURRENT ADDRESS
            placeOfBirthOrCurrentAddress(employee, employeeDTO.getSpouse().

                    getCurrentAddress().

                    getProvince(), employeeDTO.

                    getSpouse().

                    getCurrentAddress().

                    getDistrict(), employeeDTO.

                    getSpouse().

                    getCurrentAddress().

                    getCommune(), employeeDTO.

                    getSpouse().

                    getCurrentAddress().

                    getVillage(), AddressType.SPOUSE_ADDRESS, employeeDTO.getSpouse().

                    getCurrentAddress().

                    getStreetNumber(), employeeDTO.

                    getSpouse().

                    getCurrentAddress().

                    getHouseNumber());
        }
        // END SPOUSE CURRENT ADDRESS
        //SET FATHER PLACE OF BIRTH
        if (employeeDTO.getFather() != null) {
            placeOfBirthOrCurrentAddress(employee, employeeDTO.getFather().

                    getPlaceOfBirth().

                    getProvince(), employeeDTO.

                    getFather().

                    getPlaceOfBirth().

                    getDistrict(), employeeDTO.

                    getFather().

                    getPlaceOfBirth().

                    getCommune(), employeeDTO.

                    getFather().

                    getPlaceOfBirth().

                    getVillage(), AddressType.FATHER_POB, null, null);

            //END FATHER PLACE OF BIRTH
            // FATHER CURRENT ADDRESS
            placeOfBirthOrCurrentAddress(employee, employeeDTO.getFather().

                    getCurrentAddress().

                    getProvince(), employeeDTO.

                    getFather().

                    getCurrentAddress().

                    getDistrict(), employeeDTO.

                    getFather().

                    getCurrentAddress().

                    getCommune(), employeeDTO.

                    getFather().

                    getCurrentAddress().

                    getVillage(), AddressType.FATHER_ADDRESS, employeeDTO.getFather().

                    getCurrentAddress().

                    getStreetNumber(), employeeDTO.

                    getFather().

                    getCurrentAddress().

                    getHouseNumber());
        }
        // END FATHER CURRENT ADDRESS
        //SET MOTHER PLACE OF BIRTH
        if (employeeDTO.getMother() != null) {
            placeOfBirthOrCurrentAddress(employee, employeeDTO.getMother().

                    getPlaceOfBirth().

                    getProvince(), employeeDTO.

                    getMother().

                    getPlaceOfBirth().

                    getDistrict(), employeeDTO.

                    getMother().

                    getPlaceOfBirth().

                    getCommune(), employeeDTO.

                    getMother().

                    getPlaceOfBirth().

                    getVillage(), AddressType.MOTHER_POB, null, null);

            //END MOTHER PLACE OF BIRTH
            // MOTHER CURRENT ADDRESS
            placeOfBirthOrCurrentAddress(employee, employeeDTO.getMother().

                    getCurrentAddress().

                    getProvince(), employeeDTO.

                    getMother().

                    getCurrentAddress().

                    getDistrict(), employeeDTO.

                    getMother().

                    getCurrentAddress().

                    getCommune(), employeeDTO.

                    getMother().

                    getCurrentAddress().

                    getVillage(), AddressType.MOTHER_ADDRESS, employeeDTO.getMother().

                    getCurrentAddress().

                    getStreetNumber(), employeeDTO.

                    getMother().

                    getCurrentAddress().

                    getHouseNumber());
            // END MOTHER CURRENT ADDRESS
        }

        //UNIVERISTY SKILL
        if (employeeDTO.getUniversityMajorList() != null) {
            employeeDTO.getUniversityMajorList().

                    forEach(skill ->

                    {
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
        }
        //END UNIVERSITY SKILL
        //START FOREIGN LANGUAGES
        if (employeeDTO.getForeignLangList() != null) {
            employeeDTO.getForeignLangList().

                    forEach(lang ->

                    {
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
        }

        employeeRepo.save(employee);

    }


//    @Transactional
//    @Override
//    public void save(EmployeeDTO employeeDTO) {
//        Employee employee = employeeMapper.fromEmployeeDto(employeeDTO);
//        User user = userRepo.findUserById(employeeDTO.getUserId());
//        employee.setUser(user);
//
//
//        employeeRepo.save(employee);
//        if (employeeDTO.getSpouse() != null) {
//            Spouse sp = employee.getSpouse();
//            sp.setEmployee(employee);
//            List<SpouseChildren> spChildrenList = new ArrayList<>();
//            if (employeeDTO.getSpouse().getChildrenList() != null) {
//                employeeDTO.getSpouse().getChildrenList().forEach(child -> {
//                    SpouseChildren spChild = new SpouseChildren();
//                    spChild.setChildFullName(child.getFullName());
//                    spChild.setChildGender(child.getGender());
//                    spChild.setChildDateOfBirth(child.getDateOfBirth());
//                    spChild.setChildJob(child.getJob());
//                    spChild.setSpouse(sp);
//                    sp.getChildren().add(spChild);
//                    spChildrenList.add(spChild);
//                });
//                sp.setChildren(spChildrenList);
//
//            }
//            spouseRepo.save(sp);
//
//        }
//        if (employeeDTO.getFather() != null) {
//            Father fa = employee.getFather();
//            fa.setEmployee(employee);
//            fatherRepo.save(fa);
//        }
//        if (employeeDTO.getMother() != null) {
//            Mother mo = employee.getMother();
//            mo.setEmployee(employee);
//            motherRepo.save(mo);
//        }
//
//    }

    @Override
    public Employee findByUserIdAndEmployeeId(Integer userId, Integer employeeId) {
        return employeeRepo.findByIdAndUserId(userId, employeeId);
    }


    private void placeOfBirthOrCurrentAddress(Employee employee, int provinceCityId, int districtId, int communeId, int villageId, AddressType addressType, String streetNumber, String houseNumber) {

        if (provinceCityId != 0 || districtId != 0 || communeId != 0 || villageId != 0) {
            ProvinceCity provinceCity = provinceServiceImp.getProvinceById(provinceCityId);
            District district = districtServiceImp.getDistrictById(districtId);
            Commune commune = communeServiceImp.getCommuneById(communeId);
            Village village = villageServiceImp.getVillageById(villageId);
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
    }


    @Override
    public void saveEmployeeAddress(int employeeId, int addressId, AddressType addressType) {
        return;
    }


    @Override
    public Employee findById(Integer id) {
        return employeeRepo.findById(id).orElse(null);
    }

    public Employee findByUserId(Integer id) {
        return employeeRepo.findByUserId(id);
    }

    @Override
    public List<Employee> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Transactional
    @Override
    public void update(EmployeeDTO employeeDTO) {

        // Load data from the database
        Employee employee = employeeRepo.findById(employeeDTO.getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Map partial update from DTO to entity
        employeeMapper.fromEmployeeDtoPartially(employeeDTO, employee);

        // Handle spouse and spouse's children
//        if (employeeDTO.getSpouse() != null) {
//            SpouseDTO spouseDTO = employeeDTO.getSpouse();
//            Spouse spouse = null;
//
//            // Check if spouse already exists
//            spouse = new Spouse();
//            spouse.setEmployee(employee);
//            employee.setSpouse(spouse);
//
//            // Update spouse details from DTO
//            spouse.setFullName(spouseDTO.getFullName());
//
//            // Handle children update
//            List<SpouseChildren> existingChildren = spouse.getChildren();
//            existingChildren.clear(); // Clear existing children list
//
//            if (spouseDTO.getChildrenList() != null) {
//                Spouse finalSpouse = spouse;
//                spouseDTO.getChildrenList().forEach(childDTO -> {
//                    SpouseChildren spChild = new SpouseChildren();
//                    spChild.setChildFullName(childDTO.getFullName());
//                    spChild.setChildGender(childDTO.getGender());
//                    spChild.setChildDateOfBirth(childDTO.getDateOfBirth());
//                    spChild.setChildJob(childDTO.getJob());
//                    spChild.setSpouse(finalSpouse);
//                    existingChildren.add(spChild); // Add to existing list
//                });
//            }
//
//            // Save the updated spouse entity
//            spouseRepo.save(spouse);
//        }

        // Save the updated employee entity
        employeeRepo.save(employee);
    }


}

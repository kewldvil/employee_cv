package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.dto.*;
import com.noc.employee_cv.enums.*;
import com.noc.employee_cv.mapper.EmployeeMapper;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.provinces.*;
import com.noc.employee_cv.repositories.*;
import com.noc.employee_cv.security.UserDetailServiceImpl;
import com.noc.employee_cv.services.EmployeeService;
import com.noc.employee_cv.utils.KhmerNumberUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeRepo employeeRepo;
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
    private final EmployeeSkillRepo employeeSkillRepo;
    private final UniversitySkillServiceImp universitySkillServiceImp;
    private final EmployeeUniversitySkillServiceImp employeeUniversitySkillServiceImp;
    private final LanguageServiceImp languageServiceImp;
    private final EmployeeLanguageServiceImp employeeLanguageServiceImp;
    private final UserRepo userRepo;
    private final SpouseRepo spouseRepo;
    private final FatherRepo fatherRepo;
    private final MotherRepo motherRepo;
    private final ChildRepo childRepo;
    private final EmployeeLanguageRepo employeeLanguageRepo;
    private final DegreeLevelRepo degreeLevelRepo;
    private final LanguageRepo languageRepo;
    private final EmployeeDegreeLevelRepo employeeDegreeLevelRepo;
    private final SkillRepo skillRepo;
    private final PreviousActivityAndPositionRepo previousActivityAndPositionRepo;
    private final PolicePlateNumberCarRepo policePlateNumberCarRepo;
    private final WeaponRepo weaponRepo;
    private final AppreciationRepo appreciationRepo;
    private final VocationalTrainingRepo vocationalTrainingRepo;
    private final DepartmentRepo departmentRepo;


    @Override
    @Transactional
    public void save(EmployeeDTO employeeDTO) {
        System.out.println(employeeDTO.toString());
        System.out.println("Saving employee.....");
        Employee employee = employeeMapper.fromEmployeeDto(employeeDTO);
        Department dp = departmentRepo.findById(employeeDTO.getDepartmentId()).orElseThrow();
        setUserForEmployee(employee, employeeDTO.getUserId());
        employee.setPhoneNumber(KhmerNumberUtil.convertKhmerToLatin(employeeDTO.getPhoneNumber()));
        employee.setDepartment(dp);
        setSpouseAndChildren(employee, employeeDTO.getSpouse());
        setPolicePlateNumberCars(employee, employeeDTO.getPolicePlateNumberCars());
        setWeapons(employee, employeeDTO.getWeapons());
        setAppreciation(employee, employeeDTO.getAppreciations());
        setVocationalTraining(employee, employeeDTO.getVocationalTrainings());
        setActivityAndPosition(employee, employeeDTO.getActivityAndPositions());
        setFather(employee, employeeDTO.getFather());
        setMother(employee, employeeDTO.getMother());
        setEmployeeAddress(employee, employeeDTO);
        setEmployeeDegreeLevels(employee, employeeDTO.getDegreeLevels());
        setEmployeeLanguages(employee, employeeDTO.getEmployeeLanguages());
        setEmployeeSkill(employee, employeeDTO.getEmployeeSkills());

        employeeRepo.save(employee);

    }


    ///////////////////////
//    @Transactional
//    protected void setSpouseAndChildren(Employee employee, SpouseDTO spouseDTO) {
//        Spouse spouse = employee.getSpouse();
//        spouse.setEmployee(employee);
//        spouse.setPhoneNumber(spouseDTO.getPhoneNumber());
//        // Set spouse's children
//        Set<SpouseChildren> childrenList = new HashSet<>();
//        if (spouseDTO.getChildren() != null) {
//            for (ChildDTO childDTO : spouseDTO.getChildren()) {
//                SpouseChildren child = new SpouseChildren();
//                child.setFullName(childDTO.getFullName());
//                child.setGender(childDTO.getGender());
//                child.setDateOfBirth(childDTO.getDateOfBirth());
//                child.setJob(childDTO.getJob());
//                child.setSpouse(spouse);
//                childrenList.add(child);
//            }
//        }
//        spouse.setChildren(childrenList);
//        setSpousePlaceOfBirth(spouse, spouseDTO.getPlaceOfBirth(), AddressType.SPOUSE_POB);
//        setSpouseCurrentAddress(spouse, spouseDTO.getCurrentAddress(), AddressType.SPOUSE_ADDRESS);
////        spouseRepo.save(spouse);
//    }

    @Transactional
    protected void setSpouseAndChildren(Employee employee, SpouseDTO spouseDTO) {
        Spouse spouse = employee.getSpouse();
        if (employee.getIsMarried()) {
            if (spouse == null) {
                // If the spouse does not exist, create a new instance
                spouse = new Spouse();
                spouse.setEmployee(employee);
                employee.setSpouse(spouse);
            }

            // Update spouse information
            spouse.setFullName(spouseDTO.getFullName());
            spouse.setDateOfBirth(spouseDTO.getDateOfBirth());
            spouse.setJob(spouseDTO.getJob());
            spouse.setIsAlive(spouseDTO.getIsAlive());
            spouse.setPhoneNumber(KhmerNumberUtil.convertKhmerToLatin(spouseDTO.getPhoneNumber()));
            spouse.setEmployee(employee);

            // Save or update the spouse entity to ensure it's managed and has an ID
            spouse = spouseRepo.save(spouse);

            // Handle the spouse's children
            Set<SpouseChildren> existingChildren = spouse.getChildren() != null ? spouse.getChildren() : new HashSet<>();
            Set<Integer> updatedChildIds = new HashSet<>();
            Set<SpouseChildren> childrenToRemove = new HashSet<>(existingChildren);

            if (spouseDTO.getChildren() != null) {
                for (ChildDTO childDTO : spouseDTO.getChildren()) {
                    SpouseChildren child = null;
                    if (childDTO.getId() != null) {
                        // Find the existing child by ID if it exists
                        for (SpouseChildren existingChild : existingChildren) {
                            if (existingChild.getId().equals(childDTO.getId())) {
                                child = existingChild;
                                break;
                            }
                        }
                    }

                    if (child == null) {
                        // Create a new child entity if it does not exist
                        child = new SpouseChildren();
                        child.setSpouse(spouse);
                    }

                    child.setFullName(childDTO.getFullName());
                    child.setGender(childDTO.getGender());
                    child.setDateOfBirth(childDTO.getDateOfBirth());
                    child.setJob(childDTO.getJob());

                    // Save the child to ensure it has an ID and add to updated children
                    childRepo.save(child);
                    updatedChildIds.add(child.getId());
                    childrenToRemove.remove(child);
                }
            }

            // Remove children that are no longer present in the DTO
            existingChildren.removeAll(childrenToRemove);
            childRepo.deleteAll(childrenToRemove);

            // Persist the remaining children and add any new ones
            for (SpouseChildren child : existingChildren) {
                childRepo.save(child);
            }

            // Update the spouse's children set with the updated children
            spouse.getChildren().clear();
            spouse.getChildren().addAll(existingChildren);

            setSpousePlaceOfBirth(spouse, spouseDTO.getPlaceOfBirth(), AddressType.SPOUSE_POB);
            setSpouseCurrentAddress(spouse, spouseDTO.getCurrentAddress(), AddressType.SPOUSE_ADDRESS);

            // Save or update the spouse with the children and addresses
            spouseRepo.save(spouse);
        } else {
            // If employee is not married, delete spouse and children
            if (spouse != null) {
                // Delete spouse from repository
                spouseRepo.delete(spouse);
                // Remove reference to spouse from employee
                employee.setSpouse(null);


            }

            // Delete all children related to the employee if any
//            Set<SpouseChildren> children = employee.getSpouse().getChildren();
//            if (children != null && !children.isEmpty()) {
//                // Delete children from repository
//                childRepo.deleteAll(children);
//                // Remove reference to children from employee
//                employee.getSpouse().setChildren(null);
//            }

            // Save the updated employee entity
            employeeRepo.save(employee);
        }

    }


    @Transactional
    protected void setMother(Employee employee, ParentDTO motherDTO) {
        if (motherDTO != null) {
            Mother mother = employee.getMother();

            if (mother == null) {
                // If the mother does not exist, create a new instance
                mother = new Mother();
                mother.setEmployee(employee);
                employee.setMother(mother);
            }

            mother.setPhoneNumber(KhmerNumberUtil.convertKhmerToLatin(motherDTO.getPhoneNumber()));

            // Handle mother's place of birth and current address
            setMotherPlaceOfBirth(mother, motherDTO.getPlaceOfBirth(), AddressType.MOTHER_POB);
            setMotherCurrentAddress(mother, motherDTO.getCurrentAddress(), AddressType.MOTHER_ADDRESS);

            // Ensure the mother has the correct Employee reference
            mother.setEmployee(employee);

            // Save the mother entity
            motherRepo.save(mother);
        }
    }

    @Transactional
    protected void setFather(Employee employee, ParentDTO fatherDTO) {
        if (fatherDTO != null) {
            Father father = employee.getFather();

            if (father == null) {
                // If the father does not exist, create a new instance
                father = new Father();
                father.setEmployee(employee);
                employee.setFather(father);
            }

            father.setPhoneNumber(KhmerNumberUtil.convertKhmerToLatin(fatherDTO.getPhoneNumber()));

            // Handle father's place of birth and current address
            setFatherPlaceOfBirth(father, fatherDTO.getPlaceOfBirth(), AddressType.FATHER_POB);
            setFatherCurrentAddress(father, fatherDTO.getCurrentAddress(), AddressType.FATHER_ADDRESS);

            // Ensure the father has the correct Employee reference
            father.setEmployee(employee);

            // Save the father entity
            fatherRepo.save(father);
        }
    }

    private void setEmployeeAddress(Employee employee, EmployeeDTO employeeDTO) {
        setEmployeePlaceOfBirth(employee, employeeDTO.getPlaceOfBirth(), AddressType.POB);
        setEmployeeCurrentAddress(employee, employeeDTO.getCurrentAddress(), AddressType.CURRENT_ADDRESS);
    }


    private void setEmployeePlaceOfBirth(Employee employee, AddressDTO addressDTO, AddressType addressType) {
        if (addressDTO != null) {
            Address address = new Address();
            address.setAddressType(addressType);
            address.setStreetNumber(addressDTO.getStreetNumber());
            address.setHouseNumber(addressDTO.getHouseNumber());

            // Retrieve and set ProvinceCity
            ProvinceCity provinceCity = provinceServiceImp.getProvinceById(addressDTO.getProvince());
            if (provinceCity != null) {
                address.getProvinces().add(provinceCity);
            } else {
                // Handle error: ProvinceCity not found
                return;
            }

            // Retrieve and set District
            District district = districtServiceImp.getDistrictById(addressDTO.getDistrict());
            if (district != null) {
                address.getDistricts().add(district);
            } else {
                // Handle error: District not found
                return;
            }

            // Retrieve and set Commune
            Commune commune = communeServiceImp.getCommuneById(addressDTO.getCommune());
            if (commune != null) {
                address.getCommunes().add(commune);
            } else {
                // Handle error: Commune not found
                return;
            }

            // Retrieve and set Village
            Village village = villageServiceImp.getVillageById(addressDTO.getVillage());
            if (village != null) {
                address.getVillages().add(village);
            } else {
                // Handle error: Village not found
                return;
            }

            // Save the Address entity
//            addressRepo.save(address);

            // Set the place of birth for the Employee
            employee.setPlaceOfBirth(address);
            // Save the Employee entity to update the place of birth
//            employeeRepo.save(employee);
        }
    }

    private void setEmployeeCurrentAddress(Employee employee, AddressDTO addressDTO, AddressType addressType) {
        if (addressDTO != null) {
            Address address = new Address();
            address.setAddressType(addressType);
            address.setStreetNumber(addressDTO.getStreetNumber());
            address.setHouseNumber(addressDTO.getHouseNumber());

            // Retrieve and set ProvinceCity
            ProvinceCity provinceCity = provinceServiceImp.getProvinceById(addressDTO.getProvince());
            if (provinceCity != null) {
                address.getProvinces().add(provinceCity);
            } else {
                // Handle error: ProvinceCity not found
                return;
            }

            // Retrieve and set District
            District district = districtServiceImp.getDistrictById(addressDTO.getDistrict());
            if (district != null) {
                address.getDistricts().add(district);
            } else {
                // Handle error: District not found
                return;
            }

            // Retrieve and set Commune
            Commune commune = communeServiceImp.getCommuneById(addressDTO.getCommune());
            if (commune != null) {
                address.getCommunes().add(commune);
            } else {
                // Handle error: Commune not found
                return;
            }

            // Retrieve and set Village
            Village village = villageServiceImp.getVillageById(addressDTO.getVillage());
            if (village != null) {
                address.getVillages().add(village);
            } else {
                // Handle error: Village not found
                return;
            }

            // Save the Address entity
//            addressRepo.save(address);

            // Set the place of birth for the Employee
            employee.setCurrentAddress(address);
            // Save the Employee entity to update the place of birth
//            employeeRepo.save(employee);
        }
    }

    private void setSpousePlaceOfBirth(Spouse spouse, AddressDTO addressDTO, AddressType addressType) {
        if (addressDTO != null) {
            if (addressDTO.getProvince() == 0 && addressDTO.getDistrict() == 0 && addressDTO.getCommune() == 0 && addressDTO.getVillage() == 0) {
                spouse.setPlaceOfBirth(null);
                // Save the spouse entity to update the current address
//            spouseRepo.save(spouse);
                return;
            }
            Address address = new Address();
            address.setAddressType(addressType);
            address.setStreetNumber(addressDTO.getStreetNumber());
            address.setHouseNumber(addressDTO.getHouseNumber());

            // Retrieve and set ProvinceCity
            ProvinceCity provinceCity = provinceServiceImp.getProvinceById(addressDTO.getProvince());
            if (provinceCity != null) {
                address.getProvinces().add(provinceCity);
            } else {
                // Handle error: ProvinceCity not found
                return;
            }

            // Retrieve and set District
            District district = districtServiceImp.getDistrictById(addressDTO.getDistrict());
            if (district != null) {
                address.getDistricts().add(district);
            } else {
                // Handle error: District not found
                return;
            }

            // Retrieve and set Commune
            Commune commune = communeServiceImp.getCommuneById(addressDTO.getCommune());
            if (commune != null) {
                address.getCommunes().add(commune);
            } else {
                // Handle error: Commune not found
                return;
            }

            // Retrieve and set Village
            Village village = villageServiceImp.getVillageById(addressDTO.getVillage());
            if (village != null) {
                address.getVillages().add(village);
            } else {
                // Handle error: Village not found
                return;
            }

            // Save the Address entity
//            addressRepo.save(address);

            // Set the place of birth for the Employee
            spouse.setPlaceOfBirth(address);
            // Save the Employee entity to update the place of birth
//            employeeRepo.save(employee);
        }
    }

    private void setSpouseCurrentAddress(Spouse spouse, AddressDTO addressDTO, AddressType addressType) {
        if (addressDTO != null) {
            // Check if all IDs are 0 and delete current address if they are
            if (addressDTO.getProvince() == 0 && addressDTO.getDistrict() == 0 && addressDTO.getCommune() == 0 && addressDTO.getVillage() == 0) {
                spouse.setCurrentAddress(null);
                // Save the spouse entity to update the current address
//            spouseRepo.save(spouse);
                return;
            }
            Address address = new Address();
            address.setAddressType(addressType);
            address.setStreetNumber(addressDTO.getStreetNumber());
            address.setHouseNumber(addressDTO.getHouseNumber());

            // Retrieve and set ProvinceCity
            ProvinceCity provinceCity = provinceServiceImp.getProvinceById(addressDTO.getProvince());
            if (provinceCity != null) {
                address.getProvinces().add(provinceCity);
            } else {
                // Handle error: ProvinceCity not found
                return;
            }

            // Retrieve and set District
            District district = districtServiceImp.getDistrictById(addressDTO.getDistrict());
            if (district != null) {
                address.getDistricts().add(district);
            } else {
                // Handle error: District not found
                return;
            }

            // Retrieve and set Commune
            Commune commune = communeServiceImp.getCommuneById(addressDTO.getCommune());
            if (commune != null) {
                address.getCommunes().add(commune);
            } else {
                // Handle error: Commune not found
                return;
            }

            // Retrieve and set Village
            Village village = villageServiceImp.getVillageById(addressDTO.getVillage());
            if (village != null) {
                address.getVillages().add(village);
            } else {
                // Handle error: Village not found
                return;
            }

            // Save the Address entity
//            addressRepo.save(address);

            // Set the place of birth for the Employee
            spouse.setCurrentAddress(address);
            // Save the Employee entity to update the place of birth
//            employeeRepo.save(employee);
        }
    }

    private void setFatherPlaceOfBirth(Father father, AddressDTO addressDTO, AddressType addressType) {
        if (addressDTO != null) {
            if (addressDTO.getProvince() == 0 && addressDTO.getDistrict() == 0 && addressDTO.getCommune() == 0 && addressDTO.getVillage() == 0) {
                father.setPlaceOfBirth(null);
                // Save the spouse entity to update the current address
//            spouseRepo.save(spouse);
                return;
            }
            Address address = new Address();
            address.setAddressType(addressType);
            address.setStreetNumber(addressDTO.getStreetNumber());
            address.setHouseNumber(addressDTO.getHouseNumber());

            // Retrieve and set ProvinceCity
            ProvinceCity provinceCity = provinceServiceImp.getProvinceById(addressDTO.getProvince());
            if (provinceCity != null) {
                address.getProvinces().add(provinceCity);
            } else {
                // Handle error: ProvinceCity not found
                return;
            }

            // Retrieve and set District
            District district = districtServiceImp.getDistrictById(addressDTO.getDistrict());
            if (district != null) {
                address.getDistricts().add(district);
            } else {
                // Handle error: District not found
                return;
            }

            // Retrieve and set Commune
            Commune commune = communeServiceImp.getCommuneById(addressDTO.getCommune());
            if (commune != null) {
                address.getCommunes().add(commune);
            } else {
                // Handle error: Commune not found
                return;
            }

            // Retrieve and set Village
            Village village = villageServiceImp.getVillageById(addressDTO.getVillage());
            if (village != null) {
                address.getVillages().add(village);
            } else {
                // Handle error: Village not found
                return;
            }

            // Save the Address entity
//            addressRepo.save(address);

            // Set the place of birth for the Employee
            father.setPlaceOfBirth(address);
            // Save the Employee entity to update the place of birth
//            employeeRepo.save(employee);
        }
    }

    private void setFatherCurrentAddress(Father father, AddressDTO addressDTO, AddressType addressType) {
        if (addressDTO != null) {
            if (addressDTO.getProvince() == 0 && addressDTO.getDistrict() == 0 && addressDTO.getCommune() == 0 && addressDTO.getVillage() == 0) {
                father.setCurrentAddress(null);
                // Save the spouse entity to update the current address
//            spouseRepo.save(spouse);
                return;
            }
            Address address = new Address();
            address.setAddressType(addressType);
            address.setStreetNumber(addressDTO.getStreetNumber());
            address.setHouseNumber(addressDTO.getHouseNumber());

            // Retrieve and set ProvinceCity
            ProvinceCity provinceCity = provinceServiceImp.getProvinceById(addressDTO.getProvince());
            if (provinceCity != null) {
                address.getProvinces().add(provinceCity);
            } else {
                // Handle error: ProvinceCity not found
                return;
            }

            // Retrieve and set District
            District district = districtServiceImp.getDistrictById(addressDTO.getDistrict());
            if (district != null) {
                address.getDistricts().add(district);
            } else {
                // Handle error: District not found
                return;
            }

            // Retrieve and set Commune
            Commune commune = communeServiceImp.getCommuneById(addressDTO.getCommune());
            if (commune != null) {
                address.getCommunes().add(commune);
            } else {
                // Handle error: Commune not found
                return;
            }

            // Retrieve and set Village
            Village village = villageServiceImp.getVillageById(addressDTO.getVillage());
            if (village != null) {
                address.getVillages().add(village);
            } else {
                // Handle error: Village not found
                return;
            }

            // Save the Address entity
//            addressRepo.save(address);

            // Set the place of birth for the Employee
            father.setCurrentAddress(address);
            // Save the Employee entity to update the place of birth
//            employeeRepo.save(employee);
        }
    }

    private void setMotherPlaceOfBirth(Mother mother, AddressDTO addressDTO, AddressType addressType) {
        if (addressDTO != null) {
            if (addressDTO.getProvince() == 0 && addressDTO.getDistrict() == 0 && addressDTO.getCommune() == 0 && addressDTO.getVillage() == 0) {
                mother.setPlaceOfBirth(null);
                // Save the spouse entity to update the current address
//            spouseRepo.save(spouse);
                return;
            }
            Address address = new Address();
            address.setAddressType(addressType);
            address.setStreetNumber(addressDTO.getStreetNumber());
            address.setHouseNumber(addressDTO.getHouseNumber());

            // Retrieve and set ProvinceCity
            ProvinceCity provinceCity = provinceServiceImp.getProvinceById(addressDTO.getProvince());
            if (provinceCity != null) {
                address.getProvinces().add(provinceCity);
            } else {
                // Handle error: ProvinceCity not found
                return;
            }

            // Retrieve and set District
            District district = districtServiceImp.getDistrictById(addressDTO.getDistrict());
            if (district != null) {
                address.getDistricts().add(district);
            } else {
                // Handle error: District not found
                return;
            }

            // Retrieve and set Commune
            Commune commune = communeServiceImp.getCommuneById(addressDTO.getCommune());
            if (commune != null) {
                address.getCommunes().add(commune);
            } else {
                // Handle error: Commune not found
                return;
            }

            // Retrieve and set Village
            Village village = villageServiceImp.getVillageById(addressDTO.getVillage());
            if (village != null) {
                address.getVillages().add(village);
            } else {
                // Handle error: Village not found
                return;
            }

            // Save the Address entity
//            addressRepo.save(address);

            // Set the place of birth for the Employee
            mother.setPlaceOfBirth(address);
            // Save the Employee entity to update the place of birth
//            employeeRepo.save(employee);
        }
    }

    private void setMotherCurrentAddress(Mother mother, AddressDTO addressDTO, AddressType addressType) {
        if (addressDTO != null) {
            if (addressDTO.getProvince() == 0 && addressDTO.getDistrict() == 0 && addressDTO.getCommune() == 0 && addressDTO.getVillage() == 0) {
                mother.setCurrentAddress(null);
                // Save the spouse entity to update the current address
//            spouseRepo.save(spouse);
                return;
            }
            Address address = new Address();
            address.setAddressType(addressType);
            address.setStreetNumber(addressDTO.getStreetNumber());
            address.setHouseNumber(addressDTO.getHouseNumber());

            // Retrieve and set ProvinceCity
            ProvinceCity provinceCity = provinceServiceImp.getProvinceById(addressDTO.getProvince());
            if (provinceCity != null) {
                address.getProvinces().add(provinceCity);
            } else {
                // Handle error: ProvinceCity not found
                return;
            }

            // Retrieve and set District
            District district = districtServiceImp.getDistrictById(addressDTO.getDistrict());
            if (district != null) {
                address.getDistricts().add(district);
            } else {
                // Handle error: District not found
                return;
            }

            // Retrieve and set Commune
            Commune commune = communeServiceImp.getCommuneById(addressDTO.getCommune());
            if (commune != null) {
                address.getCommunes().add(commune);
            } else {
                // Handle error: Commune not found
                return;
            }

            // Retrieve and set Village
            Village village = villageServiceImp.getVillageById(addressDTO.getVillage());
            if (village != null) {
                address.getVillages().add(village);
            } else {
                // Handle error: Village not found
                return;
            }

            // Save the Address entity
//            addressRepo.save(address);

            // Set the place of birth for the Employee
            mother.setCurrentAddress(address);
            // Save the Employee entity to update the place of birth
//            employeeRepo.save(employee);
        }
    }


    private void setUniversitySkills(Employee employee, List<UniversitySkillDTO> universitySkillDTOList) {
        if (universitySkillDTOList != null) {
            for (UniversitySkillDTO skillDTO : universitySkillDTOList) {
                EmployeeUniversitySkill empUniSkill = new EmployeeUniversitySkill();
                UniversitySkill universitySkill = new UniversitySkill();
                universitySkill.setSkill(skillDTO.getUniversitySkill());
                empUniSkill.setUniversitySkill(universitySkill);
                empUniSkill.setEmployee(employee);
                employeeSkillRepo.save(empUniSkill);
            }
        }
    }

    @Transactional
    protected void setEmployeeLanguages(Employee employee, List<EmployeeLanguageDTO> languageDTOList) {
        if (languageDTOList != null && !languageDTOList.isEmpty()) {
            // Persist the employee first if it's not already persisted
            if (employee.getId() == null) {
                employee = employeeRepo.save(employee);
            }

            // Retrieve existing EmployeeLanguage entities
            List<EmployeeLanguage> existingLanguages = employeeLanguageRepo.findByEmployeeId(employee.getId());

            // Create a map to store existing EmployeeLanguage entities by language name
            Map<String, EmployeeLanguage> existingLanguagesMap = new HashMap<>();
            for (EmployeeLanguage lang : existingLanguages) {
                if (lang.getLanguage() != null && lang.getLanguage().getLanguage() != null) {
                    existingLanguagesMap.put(lang.getLanguage().getLanguage(), lang);
                }
            }

            // Iterate over the DTO list and update or create EmployeeLanguage entities
            for (EmployeeLanguageDTO langDTO : languageDTOList) {
                // Find existing EmployeeLanguage entity for the language
                EmployeeLanguage employeeLanguage = existingLanguagesMap.get(langDTO.getLanguageName());

                if (employeeLanguage == null) {
                    // If not found, create a new EmployeeLanguage entity
                    employeeLanguage = new EmployeeLanguage();
                    employeeLanguage.setEmployee(employee);
                    employee.getEmployeeLanguages().add(employeeLanguage);
                }

                // Find or create the Language entity
                Language language = languageRepo.findByLanguage(langDTO.getLanguageName());
                if (language == null) {
                    language = new Language();
                    language.setLanguage(langDTO.getLanguageName());
                    language = languageRepo.save(language);
                }

                // Update the EmployeeLanguage entity
                employeeLanguage.setLanguage(language);
                employeeLanguage.setLevel(langDTO.getLevel());

                // Save or update the EmployeeLanguage entity
                employeeLanguageRepo.save(employeeLanguage);
            }

            // Remove old EmployeeLanguage entities that are not present in the updated list
            for (Iterator<EmployeeLanguage> iterator = existingLanguages.iterator(); iterator.hasNext(); ) {
                EmployeeLanguage lang = iterator.next();
                if (!languageDTOList.stream().anyMatch(langDTO -> langDTO.getLanguageName().equals(lang.getLanguage().getLanguage()))) {
                    iterator.remove();
                    employeeLanguageRepo.delete(lang);
                }
            }
        }
    }


    //    @Transactional
//    protected void setEmployeeSkill(Employee employee, List<EmployeeSkillDTO> skillDTOList) {
//        Set<Skill> skills = new HashSet<>();
//        if (skillDTOList != null) {
//            for (EmployeeSkillDTO skillDTO : skillDTOList) {
//                Skill skill;
//                Integer skillId = skillDTO.getId();
//
//                if (skillId != null) {
//                    // Attempt to find the skill by its ID
//                    skill = skillRepo.findById(skillId).orElse(null);
//                } else {
//                    // If ID is null, create a new skill
//                    skill = new Skill();
//                }
//
//                // Set or update skill properties
//                skill.setSkillName(skillDTO.getSkillName());
//
//                // Save the skill entity before adding it to the set
//                skillRepo.save(skill);
//
//                // Add the skill to the set of skills
//                skills.add(skill);
//            }
//        }
//
//        // Set the skills to the employee
//        employee.setSkills(skills);
//
//        // Save the employee entity, which will persist the relationship
//        employeeRepo.save(employee);
//    }
    @Transactional
    protected void setEmployeeSkill(Employee employee, List<EmployeeSkillDTO> skillDTOList) {
        Set<Skill> skills = new HashSet<>();
        if (skillDTOList != null) {
            for (EmployeeSkillDTO skillDTO : skillDTOList) {
                Skill skill;
                Integer skillId = skillDTO.getId();

                if (skillId != null) {
                    // Attempt to find the skill by its ID
                    skill = skillRepo.findById(skillId).orElse(null);
                } else {
                    // Attempt to find the skill by its name to avoid duplicates
                    skill = skillRepo.findSkillBySkillName(skillDTO.getSkillName());

                    if (skill == null) {
                        // If no skill with the given name exists, create a new skill
                        skill = new Skill();
                        skill.setSkillName(skillDTO.getSkillName());

                        // Save the new skill entity
                        skillRepo.save(skill);
                    }
                }

                // Add the skill to the set of skills
                skills.add(skill);
            }
        }

        // Set the skills to the employee
        employee.setSkills(skills);

        // Save the employee entity, which will persist the relationship
        employeeRepo.save(employee);
    }

    private void setUserForEmployee(Employee employee, Integer userId) {
        User user = userRepo.findById(userId).orElseThrow();
        employee.setUser(user);
    }

    @Transactional
    protected void setPolicePlateNumberCars(Employee employee, List<PolicePlateNumberCarDTO> policeCarDTOList) {
        if (policeCarDTOList != null) {
            // Create a map of existing PolicePlateNumberCar for easy lookup
            Map<Integer, PolicePlateNumberCar> currentPoliceCarMap = new HashMap<>();
            for (PolicePlateNumberCar car : employee.getPolicePlateNumberCars()) {
                currentPoliceCarMap.put(car.getId(), car);
            }

            // List to hold updated police cars
            List<PolicePlateNumberCar> updatedPoliceCarList = new ArrayList<>();

            for (PolicePlateNumberCarDTO carDTO : policeCarDTOList) {
                PolicePlateNumberCar policeCar;

                if (carDTO.getId() != null && currentPoliceCarMap.containsKey(carDTO.getId())) {
                    // Update existing police car
                    policeCar = currentPoliceCarMap.get(carDTO.getId());
                    currentPoliceCarMap.remove(carDTO.getId());
                    System.out.println("Updating existing police car: " + carDTO.getId());
                } else {
                    // Create new police car
                    policeCar = new PolicePlateNumberCar();
                    policeCar.setEmployee(employee);  // Ensure the new police car is associated with the employee
                    System.out.println("Creating new police car");
                }

                policeCar.setVehicleBrand(carDTO.getVehicleBrand());
                policeCar.setVehicleNumber(carDTO.getVehicleNumber());
                updatedPoliceCarList.add(policeCar);
            }

            // Remove police cars that are no longer present in the DTO list
            for (PolicePlateNumberCar oldCar : currentPoliceCarMap.values()) {
                employee.getPolicePlateNumberCars().remove(oldCar);
                policePlateNumberCarRepo.delete(oldCar);
                System.out.println("Deleting old police car: " + oldCar.getId());
            }

            // Add or update the police cars in the employee
            employee.getPolicePlateNumberCars().clear();
            employee.getPolicePlateNumberCars().addAll(updatedPoliceCarList);

            // Ensure all updated or new police cars are associated with the employee
            for (PolicePlateNumberCar policeCar : updatedPoliceCarList) {
                policeCar.setEmployee(employee);
            }

            // Save the employee with updated police cars
            employeeRepo.save(employee);
            System.out.println("Employee police cars updated: " + employee.getPolicePlateNumberCars().size());
        }
    }


    @Transactional
    protected void setWeapons(Employee employee, List<WeaponDTO> weaponDTOList) {
        if (weaponDTOList != null) {
            // Create a map of existing Weapons for easy lookup
            Map<Integer, Weapon> currentWeaponMap = new HashMap<>();
            for (Weapon weapon : employee.getWeapons()) {
                currentWeaponMap.put(weapon.getId(), weapon);
            }

            // List to hold updated weapons
            List<Weapon> updatedWeaponList = new ArrayList<>();

            for (WeaponDTO weaponDTO : weaponDTOList) {
                Weapon weapon;

                if (weaponDTO.getId() != null && currentWeaponMap.containsKey(weaponDTO.getId())) {
                    // Update existing weapon
                    weapon = currentWeaponMap.get(weaponDTO.getId());
                    currentWeaponMap.remove(weaponDTO.getId());
                    System.out.println("Updating existing weapon: " + weaponDTO.getId());
                } else {
                    // Create new weapon
                    weapon = new Weapon();
                    weapon.setEmployee(employee);
                    System.out.println("Creating new weapon");
                }

                weapon.setWeaponType(weaponDTO.getWeaponType());
                weapon.setWeaponBrand(weaponDTO.getWeaponBrand());
                weapon.setWeaponSerialNumber(weaponDTO.getWeaponSerialNumber());
                updatedWeaponList.add(weapon);
            }

            // Remove weapons that are no longer present in the DTO list
            for (Weapon oldWeapon : currentWeaponMap.values()) {
                employee.getWeapons().remove(oldWeapon);
                weaponRepo.delete(oldWeapon);
                System.out.println("Deleting old weapon: " + oldWeapon.getId());
            }

            // Add or update the weapons in the employee
            employee.getWeapons().clear();
            employee.getWeapons().addAll(updatedWeaponList);


            for (Weapon weapon : updatedWeaponList) {
                weapon.setEmployee(employee);
            }

            // Save the employee with updated weapons
            employeeRepo.save(employee);
            System.out.println("Employee weapons updated: " + employee.getWeapons().size());
        }
    }


//    private void setEmployeeDegreeLevels(Employee employee, List<EmployeeDegreeLevelDTO> educationDTOList) {
//        if (educationDTOList != null) {
//            if (employee.getId() == null) {
//                employee = employeeRepo.save(employee);
//            }
//
//            // Create a set to store the new degree levels
//            Set<EmployeeDegreeLevel> employeeDegreeLevels = new HashSet<>();
//
//            for (EmployeeDegreeLevelDTO educationDTO : educationDTOList) {
//                // Create a new DegreeLevel from the DTO
//                DegreeLevel degreeLevel = degreeLevelRepo.findByEducationLevel(educationDTO.getEducationLevel());
//                if (degreeLevel == null) {
//                    degreeLevel = new DegreeLevel();
//                    degreeLevel.setEducationLevel(educationDTO.getEducationLevel()); // Correct camelCase method
//                    degreeLevel = degreeLevelRepo.save(degreeLevel);
//                }
//                EmployeeDegreeLevel employeeDegreeLevel = new EmployeeDegreeLevel();
//                employeeDegreeLevel.setDegreeLevel(degreeLevel);
//                employeeDegreeLevel.setEmployee(employee);
//                employeeDegreeLevel.setIsChecked(educationDTO.getIsChecked());
//
//                employeeDegreeLevels.add(employeeDegreeLevel);
//
//                employee.getEmployeeDegreeLevels().add(employeeDegreeLevel);
//                degreeLevel.getEmployeeDegreeLevels().add(employeeDegreeLevel);
//                employeeDegreeLevelRepo.save(employeeDegreeLevel);
//            }
//            employee.setEmployeeDegreeLevels(employeeDegreeLevels);
//        }
//    }


    @Transactional
    protected void setEmployeeDegreeLevels(Employee employee, List<EmployeeDegreeLevelDTO> educationDTOList) {
        if (educationDTOList != null && !educationDTOList.isEmpty()) {
            if (employee.getId() == null) {
                // If the employee does not exist, save it first
                employee = employeeRepo.save(employee);
                System.out.println("New employee saved: " + employee.getId());
            }

            // Create a map of existing EmployeeDegreeLevels for easy lookup
            Map<Integer, EmployeeDegreeLevel> currentDegreeLevelsMap = new HashMap<>();
            for (EmployeeDegreeLevel degreeLevel : employee.getEmployeeDegreeLevels()) {
                currentDegreeLevelsMap.put(degreeLevel.getDegreeLevel().getId(), degreeLevel);
            }

            // Create a set to store the updated degree levels
            Set<EmployeeDegreeLevel> updatedDegreeLevels = new HashSet<>();

            for (EmployeeDegreeLevelDTO educationDTO : educationDTOList) {
                // Find or create the DegreeLevel entity
                DegreeLevel degreeLevel = degreeLevelRepo.findByEducationLevel(educationDTO.getEducationLevel());
                if (degreeLevel == null) {
                    degreeLevel = new DegreeLevel();
                    degreeLevel.setEducationLevel(educationDTO.getEducationLevel());
                    degreeLevel = degreeLevelRepo.save(degreeLevel);
                    System.out.println("New degree level saved: " + degreeLevel.getId());
                }

                // Check if the EmployeeDegreeLevel already exists
                EmployeeDegreeLevel employeeDegreeLevel;
                if (currentDegreeLevelsMap.containsKey(degreeLevel.getId())) {
                    // Update existing EmployeeDegreeLevel
                    employeeDegreeLevel = currentDegreeLevelsMap.get(degreeLevel.getId());
                    employeeDegreeLevel.setIsChecked(educationDTO.getIsChecked());
                    currentDegreeLevelsMap.remove(degreeLevel.getId());
                    System.out.println("Updated existing degree level: " + employeeDegreeLevel.getId());
                } else {
                    // Create new EmployeeDegreeLevel
                    employeeDegreeLevel = new EmployeeDegreeLevel();
                    employeeDegreeLevel.setDegreeLevel(degreeLevel);
                    employeeDegreeLevel.setEmployee(employee);
                    employeeDegreeLevel.setIsChecked(educationDTO.getIsChecked());
                    System.out.println("Created new EmployeeDegreeLevel: " + employeeDegreeLevel.getId());
                }

                // Add the EmployeeDegreeLevel to the set
                updatedDegreeLevels.add(employeeDegreeLevel);
                degreeLevel.getEmployeeDegreeLevels().add(employeeDegreeLevel);

                // Save the EmployeeDegreeLevel
                employeeDegreeLevelRepo.save(employeeDegreeLevel);
            }

            // Remove old degree levels that are not present in the updated list
            employee.getEmployeeDegreeLevels().clear();
            employee.getEmployeeDegreeLevels().addAll(updatedDegreeLevels);

            // Save the Employee with the updated degree levels
            employeeRepo.save(employee);
            System.out.println("Employee degree levels updated: " + employee.getEmployeeDegreeLevels().size());
        }
    }


    @Transactional
    protected void setAppreciation(Employee employee, List<AppreciationDTO> appreciationDTOList) {
        if (appreciationDTOList != null && !appreciationDTOList.isEmpty()) {
            // Create a map of existing Appreciations for easy lookup
            Map<Integer, Appreciation> currentAppreciationMap = new HashMap<>();
            for (Appreciation appreciation : employee.getAppreciations()) {
                currentAppreciationMap.put(appreciation.getId(), appreciation);
            }

            // List to hold updated appreciations
            List<Appreciation> updatedAppreciationList = new ArrayList<>();

            for (AppreciationDTO appreciationDTO : appreciationDTOList) {
                Appreciation appreciation;

                if (appreciationDTO.getId() != null && currentAppreciationMap.containsKey(appreciationDTO.getId())) {
                    // Update existing appreciation
                    appreciation = currentAppreciationMap.get(appreciationDTO.getId());
                    currentAppreciationMap.remove(appreciationDTO.getId());
                    System.out.println("Updating existing appreciation: " + appreciationDTO.getId());
                } else {
                    // Create new appreciation
                    appreciation = new Appreciation();
                    appreciation.setEmployee(employee);
                    System.out.println("Creating new appreciation");
                }

                appreciation.setAppreciationNumber(appreciationDTO.getAppreciationNumber());
                appreciation.setAppreciationDate(appreciationDTO.getAppreciationDate());
                appreciation.setAppreciation(appreciationDTO.getAppreciation().trim());
                updatedAppreciationList.add(appreciation);
            }

            // Remove appreciations that are no longer present in the DTO list
            for (Appreciation oldAppreciation : currentAppreciationMap.values()) {
                employee.getAppreciations().remove(oldAppreciation);
                appreciationRepo.delete(oldAppreciation);
                System.out.println("Deleting old appreciation: " + oldAppreciation.getId());
            }

            // Add or update the appreciations in the employee
            employee.getAppreciations().clear();
            employee.getAppreciations().addAll(updatedAppreciationList);

            // Save the employee with updated appreciations
            employeeRepo.save(employee);
            System.out.println("Employee appreciations updated: " + employee.getAppreciations().size());
        }
    }


    @Transactional
    protected void setVocationalTraining(Employee employee, List<VocationalTrainingDTO> vocationalTrainingDTOList) {
        if (vocationalTrainingDTOList != null && !vocationalTrainingDTOList.isEmpty()) {
            // Create a map of existing VocationalTrainings for easy lookup
            Map<Integer, VocationalTraining> currentTrainingMap = new HashMap<>();
            for (VocationalTraining training : employee.getVocationalTrainings()) {
                currentTrainingMap.put(training.getId(), training);
            }

            // List to hold updated vocational trainings
            List<VocationalTraining> updatedVocationalTrainingList = new ArrayList<>();

            for (VocationalTrainingDTO trainingDTO : vocationalTrainingDTOList) {
                VocationalTraining vocationalTraining;

                if (trainingDTO.getId() != null && currentTrainingMap.containsKey(trainingDTO.getId())) {
                    // Update existing vocational training
                    vocationalTraining = currentTrainingMap.get(trainingDTO.getId());
                    currentTrainingMap.remove(trainingDTO.getId());
                    System.out.println("Updating existing vocational training: " + trainingDTO.getId());
                } else {
                    // Create new vocational training
                    vocationalTraining = new VocationalTraining();
                    vocationalTraining.setEmployee(employee);
                    System.out.println("Creating new vocational training");
                }

                vocationalTraining.setTrainingCenter(trainingDTO.getTrainingCenter().trim());
                vocationalTraining.setTrainingStartDate(trainingDTO.getTrainingStartDate());
                vocationalTraining.setTrainingToDate(trainingDTO.getTrainingToDate());
                vocationalTraining.setTrainingCourse(trainingDTO.getTrainingCourse().trim());
                vocationalTraining.setTrainingDuration(trainingDTO.getTrainingDuration());
                vocationalTraining.setIsNoStartDayMonth(trainingDTO.getIsNoStartDayMonth());
                vocationalTraining.setIsNoEndDayMonth(trainingDTO.getIsNoEndDayMonth());
                updatedVocationalTrainingList.add(vocationalTraining);
            }

            // Remove vocational trainings that are no longer present in the DTO list
            for (VocationalTraining oldTraining : currentTrainingMap.values()) {
                employee.getVocationalTrainings().remove(oldTraining);
                vocationalTrainingRepo.delete(oldTraining);
                System.out.println("Deleting old vocational training: " + oldTraining.getId());
            }

            // Add or update the vocational trainings in the employee
            employee.getVocationalTrainings().clear();
            employee.getVocationalTrainings().addAll(updatedVocationalTrainingList);

            // Save the employee with updated vocational trainings
            employeeRepo.save(employee);
            System.out.println("Employee vocational trainings updated: " + employee.getVocationalTrainings().size());
        }
    }


    @Transactional
    protected void setActivityAndPosition(Employee employee, List<PreviousActivityAndPositionDTO> activityAndPositionDTOList) {
        if (activityAndPositionDTOList != null) {
            // Create a map of existing PreviousActivityAndPosition for easy lookup
            Map<Integer, PreviousActivityAndPosition> currentActivityMap = new HashMap<>();
            for (PreviousActivityAndPosition activity : employee.getActivityAndPositions()) {
                currentActivityMap.put(activity.getId(), activity);
            }

            // List to hold updated activities and positions
            List<PreviousActivityAndPosition> updatedActivityList = new ArrayList<>();

            for (PreviousActivityAndPositionDTO activityDTO : activityAndPositionDTOList) {
                PreviousActivityAndPosition activity;

                if (activityDTO.getId() != null && currentActivityMap.containsKey(activityDTO.getId())) {
                    // Update existing activity
                    activity = currentActivityMap.get(activityDTO.getId());
                    currentActivityMap.remove(activityDTO.getId());
                    System.out.println("Updating existing activity: " + activityDTO.getId());
                } else {
                    // Create new activity
                    activity = new PreviousActivityAndPosition();
                    activity.setEmployee(employee);
                    System.out.println("Creating new activity");
                }

                activity.setFromDate(activityDTO.getFromDate());
                activity.setToDate(activityDTO.getToDate());
                activity.setActivityAndRank(activityDTO.getActivityAndRank().trim());
                activity.setDepartmentOrUnit(activityDTO.getDepartmentOrUnit().trim());
                activity.setIsNoStartDayMonth(activityDTO.getIsNoStartDayMonth());
                activity.setIsNoEndDayMonth(activityDTO.getIsNoEndDayMonth());
                updatedActivityList.add(activity);
            }

            // Remove old activities that are not present in the updated list
            for (PreviousActivityAndPosition oldActivity : currentActivityMap.values()) {
                employee.getActivityAndPositions().remove(oldActivity);
                previousActivityAndPositionRepo.delete(oldActivity);
                System.out.println("Deleting old activity: " + oldActivity.getId());
            }

            // Add or update the activities in the employee
            employee.getActivityAndPositions().clear();
            employee.getActivityAndPositions().addAll(updatedActivityList);

            // Save the employee with updated activities
            employeeRepo.save(employee);
            System.out.println("Employee activities updated: " + employee.getActivityAndPositions().size());
        }
    }


    @Override
    public Employee findByUserIdAndEmployeeId(Integer userId, Integer employeeId) {
        return employeeRepo.findByIdAndUserId(userId, employeeId);
    }

    @Override
    public long getTotalEmployees() {
        return userRepo.countEnabledUsers();
    }

    @Override
    public long getTotalEmployeesByWeapon() {
        return employeeRepo.countEmployeesWithWeapons();
    }

    @Override
    public long getTotalEmployeesByPoliceCar() {
        return employeeRepo.countEmployeesWithPoliceCars();
    }

    @Override
    public long getTotalEmployeesByBachelor() {
        return employeeRepo.countEmployeesWithDegreeLevelChecked(5);
    }

    @Override
    public long getTotalFemales() {
        return employeeRepo.countFemaleEmployees();
    }

    @Override
    public List<PoliceRankCountProjection> countByPoliceRanks() {
        return employeeRepo.countEmployeesByPoliceRank();
    }

    @Override
    public long getTotalTrainee() {
        return employeeRepo.countEmployeesByTrainee();
    }

    @Override
    public List<User> finderUsersByGender(String gender) {
        return userRepo.findUsersByGender(gender);
    }

    @Override
    public List<User> findUsersByDynamicParameter(String parameter, String value) {
        return List.of();
    }

    @Override
    public List<User> findAllUsersWithEmployeeAndWeapons() {
        return userRepo.findAllUsersWithEmployeeAndWeapons();
    }

    @Override
    public List<User> findAllUsersWithEmployeeAndPoliceCar() {
        return userRepo.findAllUsersWithEmployeeAndPoliceCar();
    }

    @Override
    public List<User> findEmployeeAndUserByDegree(int degreeLevelId) {
        return userRepo.findEmployeeAndUserByDegree(degreeLevelId);
    }

    @Override
    public List<User> findUserByTrainee(String trainee) {
        return userRepo.findUsersByTrainee(trainee);
    }

    @Override
    public long getTotalMaleTrainee() {
        return employeeRepo.countEmployeesByMaleTrainee();
    }

    @Override
    public long getTotalFemaleTrainee() {
        return employeeRepo.countEmployeesByFemaleTrainee();
    }

    @Override
    public List<Object[]> getEmployeeStatsByDepartment() {
        return userRepo.getUserStatsByDepartment();
    }

    @Override
    public Object[] getAllEmployeeStats() {
        return userRepo.getAllUserStats();
    }


    public long getTotalEmployeesByMaster() {
        return employeeRepo.countEmployeesWithDegreeLevelChecked(3);
    }

    public long getTotalEmployeesByDoctor() {
        return employeeRepo.countEmployeesWithDegreeLevelChecked(6);
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
        System.out.println("updating.....");
        System.out.println(employeeDTO.toString());
        Employee employee = employeeRepo.findById(employeeDTO.getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeDTO.setPhoneNumber(KhmerNumberUtil.convertKhmerToLatin(employeeDTO.getPhoneNumber()));
//        setEmployeeDegreeLevels(employee,employeeDTO.getDegreeLevels());
        // Map partial update from DTO to entity
        employeeMapper.fromEmployeeDtoPartially(employeeDTO, employee);
        Department dp = departmentRepo.findById(employeeDTO.getDepartmentId()).orElseThrow();
        employee.setDepartment(dp);
        setPolicePlateNumberCars(employee, employeeDTO.getPolicePlateNumberCars());
        setWeapons(employee, employeeDTO.getWeapons());
        setAppreciation(employee, employeeDTO.getAppreciations());
        setVocationalTraining(employee, employeeDTO.getVocationalTrainings());
        setActivityAndPosition(employee, employeeDTO.getActivityAndPositions());
        setSpouseAndChildren(employee, employeeDTO.getSpouse());
        setFather(employee, employeeDTO.getFather());
        setMother(employee, employeeDTO.getMother());
        setEmployeeAddress(employee, employeeDTO);
        setEmployeeDegreeLevels(employee, employeeDTO.getDegreeLevels());
        setEmployeeLanguages(employee, employeeDTO.getEmployeeLanguages());
        setEmployeeSkill(employee, employeeDTO.getEmployeeSkills());
        employeeRepo.save(employee);
    }


}

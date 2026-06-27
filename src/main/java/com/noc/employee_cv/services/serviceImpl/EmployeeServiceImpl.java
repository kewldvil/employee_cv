package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.dto.*;
import com.noc.employee_cv.enums.*;
import com.noc.employee_cv.mapper.EmployeeMapper;
import com.noc.employee_cv.model.*;
import com.noc.employee_cv.repository.*;
import com.noc.employee_cv.services.EmployeeService;
import com.noc.employee_cv.utils.KhmerNumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final EmployeeMapper employeeMapper;
    private final ProvinceCityServiceImpl provinceServiceImpl;
    private final DistrictServiceImpl districtServiceImpl;
    private final CommuneServiceImpl communeServiceImpl;
    private final VillageServiceImpl villageServiceImpl;
    private final UserRepo userRepo;
    private final EmployeeLanguageRepo employeeLanguageRepo;
    private final DegreeLevelRepo degreeLevelRepo;
    private final LanguageRepo languageRepo;
    private final EmployeeDegreeLevelRepo employeeDegreeLevelRepo;
    private final SkillRepo skillRepo;
    private final AddressRepo addressRepo;
    private final DepartmentRepo departmentRepo;
    private final PositionRepo positionRepo;
    private final FatherRepo fatherRepo;
    private final MotherRepo motherRepo;


    @Override
    @Transactional
    public void save(EmployeeDTO employeeDTO) {
        log.debug("Saving employee for user id {}", employeeDTO.getUserId());
        Employee employee = employeeMapper.fromEmployeeDto(employeeDTO);
        setUserForEmployee(employee, employeeDTO.getUserId());
        employee.setPhoneNumber(KhmerNumberUtil.convertKhmerToLatin(employeeDTO.getPhoneNumber()));
        employee.setDepartment(departmentRepo.getReferenceById(employeeDTO.getDepartmentId()));
        employee.setCurrentPosition(positionRepo.getReferenceById(employeeDTO.getCurrentPositionId()));
        employee.setPreviousPosition(positionRepo.getReferenceById(employeeDTO.getPreviousPositionId()));
        employee = employeeRepo.save(employee);

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
    }

    @Transactional
    private void setSpouseAndChildren(Employee employee, SpouseDTO spouseDTO) {
        Spouse spouse = employee.getSpouse();
        if (!Boolean.TRUE.equals(employee.getIsMarried())) {
            employee.setSpouse(null);
            return;
        }

        if (spouseDTO == null) {
            return;
        }

        if (spouse == null) {
            spouse = new Spouse();
            spouse.setEmployee(employee);
            employee.setSpouse(spouse);
        }

        spouse.setFullName(spouseDTO.getFullName());
        spouse.setDateOfBirth(spouseDTO.getDateOfBirth());
        spouse.setJob(spouseDTO.getJob());
        spouse.setIsAlive(spouseDTO.getIsAlive());
        spouse.setPhoneNumber(KhmerNumberUtil.convertKhmerToLatin(spouseDTO.getPhoneNumber()));
        spouse.setEmployee(employee);

        Map<Integer, SpouseChildren> childrenById = new HashMap<>();
        for (SpouseChildren child : spouse.getChildren()) {
            if (child.getId() != null) {
                childrenById.put(child.getId(), child);
            }
        }

        Set<SpouseChildren> updatedChildren = new HashSet<>();
        if (spouseDTO.getChildren() != null) {
            for (ChildDTO childDTO : spouseDTO.getChildren()) {
                SpouseChildren child = childDTO.getId() == null ? null : childrenById.get(childDTO.getId());
                if (child == null) {
                    child = new SpouseChildren();
                }
                child.setFullName(childDTO.getFullName());
                child.setGender(childDTO.getGender());
                child.setDateOfBirth(childDTO.getDateOfBirth());
                child.setJob(childDTO.getJob());
                child.setSpouse(spouse);
                updatedChildren.add(child);
            }
        }

        spouse.getChildren().clear();
        spouse.getChildren().addAll(updatedChildren);

        setSpousePlaceOfBirth(spouse, spouseDTO.getPlaceOfBirth(), AddressType.SPOUSE_POB);
        setSpouseCurrentAddress(spouse, spouseDTO.getCurrentAddress(), AddressType.SPOUSE_ADDRESS);
    }


    @Transactional
    private void setMother(Employee employee, ParentDTO motherDTO) {
        if (motherDTO != null) {
            Mother mother = employee.getMother();

            if (mother == null) {
                mother = new Mother();
                mother.setEmployee(employee);
                employee.setMother(mother);
            }

            mother.setFullName(motherDTO.getFullName());
            mother.setDateOfBirth(motherDTO.getDateOfBirth());
            mother.setJob(motherDTO.getJob());
            mother.setIsAlive(motherDTO.getIsAlive());
            mother.setPhoneNumber(KhmerNumberUtil.convertKhmerToLatin(motherDTO.getPhoneNumber()));

            setMotherPlaceOfBirth(mother, motherDTO.getPlaceOfBirth(), AddressType.MOTHER_POB);
            setMotherCurrentAddress(mother, motherDTO.getCurrentAddress(), AddressType.MOTHER_ADDRESS);

            mother.setEmployee(employee);
            motherRepo.save(mother);
        }
    }

    @Transactional
    private void setFather(Employee employee, ParentDTO fatherDTO) {
        if (fatherDTO != null) {
            Father father = employee.getFather();

            if (father == null) {
                father = new Father();
                father.setEmployee(employee);
                employee.setFather(father);
            }

            father.setFullName(fatherDTO.getFullName());
            father.setDateOfBirth(fatherDTO.getDateOfBirth());
            father.setJob(fatherDTO.getJob());
            father.setIsAlive(fatherDTO.getIsAlive());
            father.setPhoneNumber(KhmerNumberUtil.convertKhmerToLatin(fatherDTO.getPhoneNumber()));

            setFatherPlaceOfBirth(father, fatherDTO.getPlaceOfBirth(), AddressType.FATHER_POB);
            setFatherCurrentAddress(father, fatherDTO.getCurrentAddress(), AddressType.FATHER_ADDRESS);

            father.setEmployee(employee);
            fatherRepo.save(father);
        }
    }

    private void setEmployeeAddress(Employee employee, EmployeeDTO employeeDTO) {
        setEmployeePlaceOfBirth(employee, employeeDTO.getPlaceOfBirth(), AddressType.POB);
        setEmployeeCurrentAddress(employee, employeeDTO.getCurrentAddress(), AddressType.CURRENT_ADDRESS);
    }

    private void setEmployeePlaceOfBirth(Employee employee, AddressDTO addressDTO, AddressType addressType) {
        buildAddress(addressDTO, addressType, false).ifPresent(employee::setPlaceOfBirth);
    }

    private void setEmployeeCurrentAddress(Employee employee, AddressDTO addressDTO, AddressType addressType) {
        buildAddress(addressDTO, addressType, false).ifPresent(employee::setCurrentAddress);
    }

    private void setSpousePlaceOfBirth(Spouse spouse, AddressDTO addressDTO, AddressType addressType) {
        if (isEmptyAddress(addressDTO)) {
            spouse.setPlaceOfBirth(null);
            return;
        }
        buildAddress(addressDTO, addressType, true).ifPresent(spouse::setPlaceOfBirth);
    }

    private void setSpouseCurrentAddress(Spouse spouse, AddressDTO addressDTO, AddressType addressType) {
        if (isEmptyAddress(addressDTO)) {
            spouse.setCurrentAddress(null);
            return;
        }
        buildAddress(addressDTO, addressType, true).ifPresent(spouse::setCurrentAddress);
    }

    private void setFatherPlaceOfBirth(Father father, AddressDTO addressDTO, AddressType addressType) {
        if (isEmptyAddress(addressDTO)) {
            father.setPlaceOfBirth(null);
            return;
        }
        buildAddress(addressDTO, addressType, true).ifPresent(father::setPlaceOfBirth);
    }

    private void setFatherCurrentAddress(Father father, AddressDTO addressDTO, AddressType addressType) {
        if (isEmptyAddress(addressDTO)) {
            father.setCurrentAddress(null);
            return;
        }
        buildAddress(addressDTO, addressType, true).ifPresent(father::setCurrentAddress);
    }

    private void setMotherPlaceOfBirth(Mother mother, AddressDTO addressDTO, AddressType addressType) {
        if (isEmptyAddress(addressDTO)) {
            mother.setPlaceOfBirth(null);
            return;
        }
        buildAddress(addressDTO, addressType, true).ifPresent(mother::setPlaceOfBirth);
    }

    private void setMotherCurrentAddress(Mother mother, AddressDTO addressDTO, AddressType addressType) {
        if (isEmptyAddress(addressDTO)) {
            mother.setCurrentAddress(null);
            return;
        }
        buildAddress(addressDTO, addressType, true).ifPresent(mother::setCurrentAddress);
    }

    private Optional<Address> buildAddress(AddressDTO addressDTO, AddressType addressType, boolean allowEmptyAddress) {
        if (addressDTO == null || (allowEmptyAddress && isEmptyAddress(addressDTO))) {
            return Optional.empty();
        }

        ProvinceCity provinceCity = provinceServiceImpl.getProvinceById(addressDTO.getProvince());
        District district = districtServiceImpl.getDistrictById(addressDTO.getDistrict());
        Commune commune = communeServiceImpl.getCommuneById(addressDTO.getCommune());
        Village village = villageServiceImpl.getVillageById(addressDTO.getVillage());

        if (provinceCity == null || district == null || commune == null || village == null) {
            log.warn(
                    "Skipping {} address because location ids are invalid: province={}, district={}, commune={}, village={}",
                    addressType,
                    addressDTO.getProvince(),
                    addressDTO.getDistrict(),
                    addressDTO.getCommune(),
                    addressDTO.getVillage()
            );
            return Optional.empty();
        }

        Address address = new Address();
        address.setAddressType(addressType);
        address.setStreetNumber(addressDTO.getStreetNumber());
        address.setHouseNumber(addressDTO.getHouseNumber());
        address.getProvinces().add(provinceCity);
        address.getDistricts().add(district);
        address.getCommunes().add(commune);
        address.getVillages().add(village);
        return Optional.of(address);
    }

    private boolean isEmptyAddress(AddressDTO addressDTO) {
        return addressDTO == null
                || (addressDTO.getProvince() == 0
                && addressDTO.getDistrict() == 0
                && addressDTO.getCommune() == 0
                && addressDTO.getVillage() == 0);
    }

    @Transactional
    private void setEmployeeLanguages(Employee employee, List<EmployeeLanguageDTO> languageDTOList) {
        if (languageDTOList != null && !languageDTOList.isEmpty()) {
            List<EmployeeLanguage> existingLanguages = employeeLanguageRepo.findByEmployeeId(employee.getId());

            Map<String, EmployeeLanguage> existingLanguagesMap = new HashMap<>();
            for (EmployeeLanguage lang : existingLanguages) {
                if (lang.getLanguage() != null && lang.getLanguage().getLanguage() != null) {
                    existingLanguagesMap.put(lang.getLanguage().getLanguage(), lang);
                }
            }

            Set<String> requestedLanguageNames = new HashSet<>();
            for (EmployeeLanguageDTO langDTO : languageDTOList) {
                requestedLanguageNames.add(langDTO.getLanguageName());
                EmployeeLanguage employeeLanguage = existingLanguagesMap.get(langDTO.getLanguageName());

                if (employeeLanguage == null) {
                    employeeLanguage = new EmployeeLanguage();
                    employeeLanguage.setEmployee(employee);
                    employee.getEmployeeLanguages().add(employeeLanguage);
                }

                Language language = languageRepo.findByLanguage(langDTO.getLanguageName());
                if (language == null) {
                    language = new Language();
                    language.setLanguage(langDTO.getLanguageName());
                    language = languageRepo.save(language);
                }

                employeeLanguage.setLanguage(language);
                employeeLanguage.setLevel(langDTO.getLevel());
                employeeLanguageRepo.save(employeeLanguage);
            }

            for (Iterator<EmployeeLanguage> iterator = existingLanguages.iterator(); iterator.hasNext(); ) {
                EmployeeLanguage lang = iterator.next();
                if (!requestedLanguageNames.contains(lang.getLanguage().getLanguage())) {
                    iterator.remove();
                    employeeLanguageRepo.delete(lang);
                }
            }
        }
    }

    @Transactional
    private void setEmployeeSkill(Employee employee, List<EmployeeSkillDTO> skillDTOList) {
        Set<Skill> skills = new HashSet<>();
        if (skillDTOList != null) {
            for (EmployeeSkillDTO skillDTO : skillDTOList) {
                Skill skill;
                Integer skillId = skillDTO.getId();

                if (skillId != null) {
                    skill = skillRepo.findById(skillId).orElse(null);
                } else {
                    skill = skillRepo.findSkillBySkillName(skillDTO.getSkillName());

                    if (skill == null) {
                        skill = new Skill();
                        skill.setSkillName(skillDTO.getSkillName());
                        skillRepo.save(skill);
                    }
                }

                if (skill != null) {
                    skills.add(skill);
                }
            }
        }

        employee.setSkills(skills);
    }

    private void setUserForEmployee(Employee employee, Integer userId) {
        employee.setUser(userRepo.getReferenceById(userId));
    }

    @Transactional
    private void setPolicePlateNumberCars(Employee employee, List<PolicePlateNumberCarDTO> policeCarDTOList) {
        if (policeCarDTOList != null) {
            Map<Integer, PolicePlateNumberCar> currentPoliceCarMap = new HashMap<>();
            for (PolicePlateNumberCar car : employee.getPolicePlateNumberCars()) {
                currentPoliceCarMap.put(car.getId(), car);
            }

            List<PolicePlateNumberCar> updatedPoliceCarList = new ArrayList<>();

            for (PolicePlateNumberCarDTO carDTO : policeCarDTOList) {
                PolicePlateNumberCar policeCar;

                if (carDTO.getId() != null && currentPoliceCarMap.containsKey(carDTO.getId())) {
                    policeCar = currentPoliceCarMap.get(carDTO.getId());
                    currentPoliceCarMap.remove(carDTO.getId());
                } else {
                    policeCar = new PolicePlateNumberCar();
                    policeCar.setEmployee(employee);
                }

                policeCar.setVehicleBrand(carDTO.getVehicleBrand());
                policeCar.setVehicleNumber(carDTO.getVehicleNumber());
                updatedPoliceCarList.add(policeCar);
            }

            for (PolicePlateNumberCar oldCar : currentPoliceCarMap.values()) {
                employee.getPolicePlateNumberCars().remove(oldCar);
            }

            employee.getPolicePlateNumberCars().clear();
            employee.getPolicePlateNumberCars().addAll(updatedPoliceCarList);

            for (PolicePlateNumberCar policeCar : updatedPoliceCarList) {
                policeCar.setEmployee(employee);
            }

            log.debug("Employee police cars updated: {}", employee.getPolicePlateNumberCars().size());
        }
    }


    @Transactional
    private void setWeapons(Employee employee, List<WeaponDTO> weaponDTOList) {
        if (weaponDTOList != null) {
            Map<Integer, Weapon> currentWeaponMap = new HashMap<>();
            for (Weapon weapon : employee.getWeapons()) {
                currentWeaponMap.put(weapon.getId(), weapon);
            }

            List<Weapon> updatedWeaponList = new ArrayList<>();

            for (WeaponDTO weaponDTO : weaponDTOList) {
                Weapon weapon;

                if (weaponDTO.getId() != null && currentWeaponMap.containsKey(weaponDTO.getId())) {
                    weapon = currentWeaponMap.get(weaponDTO.getId());
                    currentWeaponMap.remove(weaponDTO.getId());
                } else {
                    weapon = new Weapon();
                    weapon.setEmployee(employee);
                }

                weapon.setWeaponType(weaponDTO.getWeaponType());
                weapon.setWeaponBrand(weaponDTO.getWeaponBrand());
                weapon.setWeaponSerialNumber(weaponDTO.getWeaponSerialNumber());
                updatedWeaponList.add(weapon);
            }

            for (Weapon oldWeapon : currentWeaponMap.values()) {
                employee.getWeapons().remove(oldWeapon);
            }

            employee.getWeapons().clear();
            employee.getWeapons().addAll(updatedWeaponList);


            for (Weapon weapon : updatedWeaponList) {
                weapon.setEmployee(employee);
            }

            log.debug("Employee weapons updated: {}", employee.getWeapons().size());
        }
    }

    @Transactional
    private void setEmployeeDegreeLevels(Employee employee, List<EmployeeDegreeLevelDTO> educationDTOList) {
        if (educationDTOList != null && !educationDTOList.isEmpty()) {
            Map<Integer, EmployeeDegreeLevel> currentDegreeLevelsMap = new HashMap<>();
            for (EmployeeDegreeLevel degreeLevel : employee.getEmployeeDegreeLevels()) {
                currentDegreeLevelsMap.put(degreeLevel.getDegreeLevel().getId(), degreeLevel);
            }

            Set<EmployeeDegreeLevel> updatedDegreeLevels = new HashSet<>();

            for (EmployeeDegreeLevelDTO educationDTO : educationDTOList) {
                DegreeLevel degreeLevel = degreeLevelRepo.findByEducationLevel(educationDTO.getEducationLevel());
                if (degreeLevel == null) {
                    degreeLevel = new DegreeLevel();
                    degreeLevel.setEducationLevel(educationDTO.getEducationLevel());
                    degreeLevel = degreeLevelRepo.save(degreeLevel);
                }

                EmployeeDegreeLevel employeeDegreeLevel;
                if (currentDegreeLevelsMap.containsKey(degreeLevel.getId())) {
                    employeeDegreeLevel = currentDegreeLevelsMap.get(degreeLevel.getId());
                    employeeDegreeLevel.setIsChecked(educationDTO.getIsChecked());
                    currentDegreeLevelsMap.remove(degreeLevel.getId());
                } else {
                    employeeDegreeLevel = new EmployeeDegreeLevel();
                    employeeDegreeLevel.setDegreeLevel(degreeLevel);
                    employeeDegreeLevel.setEmployee(employee);
                    employeeDegreeLevel.setIsChecked(educationDTO.getIsChecked());
                }

                updatedDegreeLevels.add(employeeDegreeLevel);
                degreeLevel.getEmployeeDegreeLevels().add(employeeDegreeLevel);
                employeeDegreeLevelRepo.save(employeeDegreeLevel);
            }

            employee.getEmployeeDegreeLevels().clear();
            employee.getEmployeeDegreeLevels().addAll(updatedDegreeLevels);

            log.debug("Employee degree levels updated: {}", employee.getEmployeeDegreeLevels().size());
        }
    }


    @Transactional
    private void setAppreciation(Employee employee, List<AppreciationDTO> appreciationDTOList) {
        if (appreciationDTOList != null && !appreciationDTOList.isEmpty()) {
            Map<Integer, Appreciation> currentAppreciationMap = new HashMap<>();
            for (Appreciation appreciation : employee.getAppreciations()) {
                currentAppreciationMap.put(appreciation.getId(), appreciation);
            }

            List<Appreciation> updatedAppreciationList = new ArrayList<>();

            for (AppreciationDTO appreciationDTO : appreciationDTOList) {
                Appreciation appreciation;

                if (appreciationDTO.getId() != null && currentAppreciationMap.containsKey(appreciationDTO.getId())) {
                    appreciation = currentAppreciationMap.get(appreciationDTO.getId());
                    currentAppreciationMap.remove(appreciationDTO.getId());
                } else {
                    appreciation = new Appreciation();
                    appreciation.setEmployee(employee);
                }

                appreciation.setAppreciationNumber(appreciationDTO.getAppreciationNumber());
                appreciation.setAppreciationDate(appreciationDTO.getAppreciationDate());
                appreciation.setAppreciation(appreciationDTO.getAppreciation().trim());
                updatedAppreciationList.add(appreciation);
            }

            for (Appreciation oldAppreciation : currentAppreciationMap.values()) {
                employee.getAppreciations().remove(oldAppreciation);
            }

            employee.getAppreciations().clear();
            employee.getAppreciations().addAll(updatedAppreciationList);

            log.debug("Employee appreciations updated: {}", employee.getAppreciations().size());
        }
    }


    @Transactional
    private void setVocationalTraining(Employee employee, List<VocationalTrainingDTO> vocationalTrainingDTOList) {
        if (vocationalTrainingDTOList != null && !vocationalTrainingDTOList.isEmpty()) {
            Map<Integer, VocationalTraining> currentTrainingMap = new HashMap<>();
            for (VocationalTraining training : employee.getVocationalTrainings()) {
                currentTrainingMap.put(training.getId(), training);
            }

            List<VocationalTraining> updatedVocationalTrainingList = new ArrayList<>();

            for (VocationalTrainingDTO trainingDTO : vocationalTrainingDTOList) {
                VocationalTraining vocationalTraining;

                if (trainingDTO.getId() != null && currentTrainingMap.containsKey(trainingDTO.getId())) {
                    vocationalTraining = currentTrainingMap.get(trainingDTO.getId());
                    currentTrainingMap.remove(trainingDTO.getId());
                } else {
                    vocationalTraining = new VocationalTraining();
                    vocationalTraining.setEmployee(employee);
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

            for (VocationalTraining oldTraining : currentTrainingMap.values()) {
                employee.getVocationalTrainings().remove(oldTraining);
            }

            employee.getVocationalTrainings().clear();
            employee.getVocationalTrainings().addAll(updatedVocationalTrainingList);

            log.debug("Employee vocational trainings updated: {}", employee.getVocationalTrainings().size());
        }
    }


    @Transactional
    private void setActivityAndPosition(Employee employee, List<PreviousActivityAndPositionDTO> activityAndPositionDTOList) {
        if (activityAndPositionDTOList != null) {
            Map<Integer, PreviousActivityAndPosition> currentActivityMap = new HashMap<>();
            for (PreviousActivityAndPosition activity : employee.getActivityAndPositions()) {
                currentActivityMap.put(activity.getId(), activity);
            }

            List<PreviousActivityAndPosition> updatedActivityList = new ArrayList<>();

            for (PreviousActivityAndPositionDTO activityDTO : activityAndPositionDTOList) {
                PreviousActivityAndPosition activity;

                if (activityDTO.getId() != null && currentActivityMap.containsKey(activityDTO.getId())) {
                    activity = currentActivityMap.get(activityDTO.getId());
                    currentActivityMap.remove(activityDTO.getId());
                } else {
                    activity = new PreviousActivityAndPosition();
                    activity.setEmployee(employee);
                }

                activity.setFromDate(activityDTO.getFromDate());
                activity.setToDate(activityDTO.getToDate());
                activity.setActivityAndRank(activityDTO.getActivityAndRank().trim());
                activity.setDepartmentOrUnit(activityDTO.getDepartmentOrUnit().trim());
                activity.setIsNoStartDayMonth(activityDTO.getIsNoStartDayMonth());
                activity.setIsNoEndDayMonth(activityDTO.getIsNoEndDayMonth());
                updatedActivityList.add(activity);
            }

            for (PreviousActivityAndPosition oldActivity : currentActivityMap.values()) {
                employee.getActivityAndPositions().remove(oldActivity);
            }

            employee.getActivityAndPositions().clear();
            employee.getActivityAndPositions().addAll(updatedActivityList);

            log.debug("Employee activities updated: {}", employee.getActivityAndPositions().size());
        }
    }


    @Override
    public Employee findByUserIdAndEmployeeId(Integer employeeId, Integer userId) {
        return initializeForResponse(employeeRepo.findByIdAndUserId(employeeId, userId));
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
        throw new UnsupportedOperationException("Dynamic employee user search is not implemented");
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
    @Transactional
    public void saveEmployeeAddress(int employeeId, int addressId, AddressType addressType) {
        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Employee not found: " + employeeId));
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new NoSuchElementException("Address not found: " + addressId));
        address.setAddressType(addressType);

        switch (addressType) {
            case POB -> employee.setPlaceOfBirth(address);
            case CURRENT_ADDRESS -> employee.setCurrentAddress(address);
            default -> throw new IllegalArgumentException("Unsupported employee address type: " + addressType);
        }
    }


    @Override
    public Employee findById(Integer id) {
        return initializeForResponse(employeeRepo.findById(id).orElse(null));
    }

    public Employee findByUserId(Integer id) {
        return initializeForResponse(employeeRepo.findByUserId(id));
    }

    public EmployeeDTO findDetailDtoByUserId(Integer id, List<UserFileDTO> fileNames) {
        return employeeMapper.toEmployeeDetailDto(initializeForResponse(employeeRepo.findByUserId(id)), fileNames);
    }

    public EmployeeDTO findDetailDtoByUserIdAndEmployeeId(Integer employeeId, Integer userId, List<UserFileDTO> fileNames) {
        return employeeMapper.toEmployeeDetailDto(initializeForResponse(employeeRepo.findByIdAndUserId(employeeId, userId)), fileNames);
    }

    private Employee initializeForResponse(Employee employee) {
        if (employee == null) {
            return null;
        }

        Hibernate.initialize(employee.getSpouse());
        if (employee.getSpouse() != null) {
            Hibernate.initialize(employee.getSpouse().getChildren());
            List<SpouseChildren> sortedChildren = employee.getSpouse().getChildren()
                    .stream()
                    .sorted(Comparator.nullsLast(
                            Comparator.comparing(SpouseChildren::getDateOfBirth, Comparator.nullsLast(Comparator.naturalOrder()))))
                    .toList();
            employee.getSpouse().setChildren(new LinkedHashSet<>(sortedChildren));
            Hibernate.initialize(employee.getSpouse().getPlaceOfBirth());
            Hibernate.initialize(employee.getSpouse().getCurrentAddress());
        }

        Hibernate.initialize(employee.getFather());
        if (employee.getFather() != null) {
            Hibernate.initialize(employee.getFather().getPlaceOfBirth());
            Hibernate.initialize(employee.getFather().getCurrentAddress());
        }

        Hibernate.initialize(employee.getMother());
        if (employee.getMother() != null) {
            Hibernate.initialize(employee.getMother().getPlaceOfBirth());
            Hibernate.initialize(employee.getMother().getCurrentAddress());
        }

        Hibernate.initialize(employee.getPlaceOfBirth());
        Hibernate.initialize(employee.getCurrentAddress());
        Hibernate.initialize(employee.getPolicePlateNumberCars());
        Hibernate.initialize(employee.getWeapons());
        Hibernate.initialize(employee.getEmployeeDegreeLevels());
        Hibernate.initialize(employee.getEmployeeLanguages());
        Hibernate.initialize(employee.getSkills());
        Hibernate.initialize(employee.getAppreciations());
        Hibernate.initialize(employee.getVocationalTrainings());
        Hibernate.initialize(employee.getActivityAndPositions());
        sortResponseCollections(employee);

        return employee;
    }

    private void sortResponseCollections(Employee employee) {
        Optional.ofNullable(employee.getVocationalTrainings())
                .ifPresent(vt -> vt.sort(Comparator.nullsLast(
                        Comparator.comparing(VocationalTraining::getTrainingStartDate, Comparator.nullsLast(Comparator.naturalOrder())))));

        Optional.ofNullable(employee.getAppreciations())
                .ifPresent(appreciations -> appreciations.sort(Comparator.nullsLast(
                        Comparator.comparing(Appreciation::getAppreciationDate, Comparator.nullsLast(Comparator.naturalOrder())))));

        Optional.ofNullable(employee.getActivityAndPositions())
                .ifPresent(activities -> activities.sort(Comparator.nullsLast(
                        Comparator.comparing(PreviousActivityAndPosition::getFromDate, Comparator.nullsLast(Comparator.naturalOrder())))));
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        employeeRepo.deleteById(id);
    }

    @Transactional
    @Override
    public void update(EmployeeDTO employeeDTO) {
        log.debug("Updating employee id {}", employeeDTO.getId());
        Employee employee = employeeRepo.findById(employeeDTO.getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employeeDTO.setPhoneNumber(KhmerNumberUtil.convertKhmerToLatin(employeeDTO.getPhoneNumber()));

        employeeMapper.updateEmployeeFromDto(employeeDTO, employee);

        employee.setCurrentPosition(positionRepo.getReferenceById(employeeDTO.getCurrentPositionId()));
        employee.setPreviousPosition(positionRepo.getReferenceById(employeeDTO.getPreviousPositionId()));
        employee.setDepartment(departmentRepo.getReferenceById(employeeDTO.getDepartmentId()));

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
    }



}

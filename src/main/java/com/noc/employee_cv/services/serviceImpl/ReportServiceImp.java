package com.noc.employee_cv.services.serviceImpl;

import net.sf.jasperreports.engine.util.JRFontNotFoundException;
import com.noc.employee_cv.dto.*;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.provinces.District;
import com.noc.employee_cv.provinces.ProvinceCity;
import com.noc.employee_cv.provinces.Village;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.utils.EmployeeUtil;
import com.noc.employee_cv.utils.KhmerNumberUtil;
import com.noc.employee_cv.utils.PhoneNumberFormatter;
import io.github.metheax.Chhankitek;
import io.github.metheax.domain.KhmerLunarDate;
import io.github.metheax.utils.ChhankitekUtils;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImp {

    private final EmployeeRepo employeeRepo;
    @Value("${file.report-dir}")
    private String REPORT_DIR;
    @Value("${file.photo-dir}")
    private String REPORT_PHOTO_PATH;

    @Autowired
    private ResourceLoader resourceLoader;

    public String exportReport(String reportFormat, Integer empId) throws FileNotFoundException, JRException {
        KhmerLunarDate khmerLunarDate = Chhankitek.toKhmerLunarDateFormat(LocalDateTime.now());
        String khmerYearString = ChhankitekUtils.convertIntegerToKhmerNumber(LocalDateTime.now().getYear());
        String khmerLunarDateString = "ឆ្នាំ" + khmerLunarDate.getLunarZodiac() + " " + khmerLunarDate.getLunarEra() + "ព.ស." + khmerLunarDate.getLunarYear();
        Employee employee = employeeRepo.findEmployeeAndUserById(empId);
        employee.setPoliceId(KhmerNumberUtil.convertToKhmerNumber(Integer.parseInt(employee.getPoliceId())));
        employee.setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getPhoneNumber()));


        if (employee.getSpouse() != null)
            employee.getSpouse().setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getSpouse().getPhoneNumber()));
        if (employee.getFather() != null)
            employee.getFather().setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getFather().getPhoneNumber()));
        if (employee.getMother() != null)
            employee.getMother().setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getMother().getPhoneNumber()));
        String childNumber = KhmerNumberUtil.convertToKhmerNumber(getNumberOfChildren(employee));
        String imageName = employee.getUser().getImageName();

        // Load file and compile
        File file;
        try {
            file = ResourceUtils.getFile("classpath:employee_report.jrxml");
            System.out.println("Report file path: " + file.getAbsolutePath()); // Log the file path
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("The report file was not found in the classpath: employee_report.jrxml");
        }

        JasperReport jasperReport;
        try {
            jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        } catch (JRException e) {
            // Instead of rethrowing the exception immediately, log the error and handle it gracefully
            String errorMsg = "Unable to compile the report file: " + file.getAbsolutePath();
            System.err.println(errorMsg); // Log the error message
            throw new JRException(errorMsg, e); // Rethrow the exception for further handling
        }

        // Create a list containing the single employee
        List<Employee> employees = Collections.singletonList(employee);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        List<VTTrainingPDFDTO> vocationalTrainings = updateVocationalTrainingKH(employee.getVocationalTrainings());
        JRBeanCollectionDataSource vocationalTrainingsDatasource = new JRBeanCollectionDataSource(vocationalTrainings);
        List<AppreciationPDFDTO> appreciations = updateAppreciationKH(employee.getAppreciations());
        JRBeanCollectionDataSource appreciationDataset = new JRBeanCollectionDataSource(appreciations);
        List<PrevActivityPDFDTO> previousActivityAndPositions = updateActivityKH(employee.getActivityAndPositions());
        JRBeanCollectionDataSource activityDataset = new JRBeanCollectionDataSource(previousActivityAndPositions);
        List<DegreeLevelInfoDTO> employeeDegreeLevels = extractDegreeLevels(employee);
        List<Weapon> empWeapons = employee.getWeapons();
        List<PolicePlateNumberCar> empVehicle = employee.getPolicePlateNumberCars();
        Set<ChildrenPDFDTO> childrenList = updateChildrenList(getChildrenList(employee));
        String universitySkill = EmployeeUtil.updateUniversitySkill(employee);
        String foreignLanguage = EmployeeUtil.updateForeignLanguage(employee);
        String spouseDOB = employee.getSpouse() == null ? "" : KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getSpouse().getDateOfBirth()));
        String fatherDOB = employee.getFather() == null ? "" : KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getFather().getDateOfBirth()));
        String motherDOB = employee.getMother() == null ? "" : KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getMother().getDateOfBirth()));
//        emp place of birth
        String empProvince = employee.getPlaceOfBirth().getProvinces().get(0).getProvince_city_name_kh();
        String empDistrict = employee.getPlaceOfBirth().getDistricts().get(0).getDistrict_name_kh();
        String empCommune = employee.getPlaceOfBirth().getCommunes().get(0).getCommune_name_kh();
        String empVillage = employee.getPlaceOfBirth().getVillages().get(0).getVillage_name_kh();
//end emp place of birth

        //        spouse place of birth
        String spouseProvince = "";
        String spouseDistrict = "";
        String spouseCommune = "";
        String spouseVillage = "";

        if (employee.getSpouse() != null && employee.getSpouse().getPlaceOfBirth() != null) {
            List<ProvinceCity> spouseProvinces = employee.getSpouse().getPlaceOfBirth().getProvinces();
            List<District> spouseDistricts = employee.getSpouse().getPlaceOfBirth().getDistricts();
            List<Commune> spouseCommunes = employee.getSpouse().getPlaceOfBirth().getCommunes();
            List<Village> spouseVillages = employee.getSpouse().getPlaceOfBirth().getVillages();

            if (!spouseProvinces.isEmpty()) {
                spouseProvince = spouseProvinces.get(0).getProvince_city_name_kh();
            }
            if (!spouseDistricts.isEmpty()) {
                spouseDistrict = spouseDistricts.get(0).getDistrict_name_kh();
            }
            if (!spouseCommunes.isEmpty()) {
                spouseCommune = spouseCommunes.get(0).getCommune_name_kh();
            }
            if (!spouseVillages.isEmpty()) {
                spouseVillage = spouseVillages.get(0).getVillage_name_kh();
            }
        }

//end spouse place of birth
        //        father place of birth
        assert employee.getFather() != null;
        String fatherProvince = employee.getFather().getPlaceOfBirth().getProvinces().get(0).getProvince_city_name_kh();
        String fatherDistrict = employee.getFather().getPlaceOfBirth().getDistricts().get(0).getDistrict_name_kh();
        String fatherCommune = employee.getFather().getPlaceOfBirth().getCommunes().get(0).getCommune_name_kh();
        String fatherVillage = employee.getFather().getPlaceOfBirth().getVillages().get(0).getVillage_name_kh();
//end father place of birth
        //        mother place of birth
        assert employee.getMother() != null;
        String motherProvince = employee.getMother().getPlaceOfBirth().getProvinces().get(0).getProvince_city_name_kh();
        String motherDistrict = employee.getMother().getPlaceOfBirth().getDistricts().get(0).getDistrict_name_kh();
        String motherCommune = employee.getMother().getPlaceOfBirth().getCommunes().get(0).getCommune_name_kh();
        String motherVillage = employee.getMother().getPlaceOfBirth().getVillages().get(0).getVillage_name_kh();
//end mother place of birth
//        emp current address

        String empCurrentProvince = employee.getCurrentAddress().getProvinces().get(0).getProvince_city_name_kh();
        String empCurrentDistrict = employee.getCurrentAddress().getDistricts().get(0).getDistrict_name_kh();
        String empCurrentCommune = employee.getCurrentAddress().getCommunes().get(0).getCommune_name_kh();
        String empCurrentVillage = employee.getCurrentAddress().getVillages().get(0).getVillage_name_kh();
        String empCurrentStreetNumber = employee.getCurrentAddress().getStreetNumber();
        String empCurrentHouseNumber = employee.getCurrentAddress().getHouseNumber();
//end emp current address
//        spouse current address

        String spouseCurrentProvince = "";
        String spouseCurrentDistrict = "";
        String spouseCurrentCommune = "";
        String spouseCurrentVillage = "";
        String spouseCurrentStreetNumber = "";
        String spouseCurrentHouseNumber = "";
        String totalFemaleChildren = KhmerNumberUtil.convertToKhmerNumber(countChildrenByGender("M", employee));
        String totalMaleChildren = KhmerNumberUtil.convertToKhmerNumber(countChildrenByGender("F", employee));
        if (employee.getSpouse() != null && employee.getSpouse().getCurrentAddress() != null) {
            List<ProvinceCity> spouseCurrentProvinces = employee.getSpouse().getCurrentAddress().getProvinces();
            List<District> spouseCurrentDistricts = employee.getSpouse().getCurrentAddress().getDistricts();
            List<Commune> spouseCurrentCommunes = employee.getSpouse().getCurrentAddress().getCommunes();
            List<Village> spouseCurrentVillages = employee.getSpouse().getCurrentAddress().getVillages();

            if (!spouseCurrentProvinces.isEmpty()) {
                spouseCurrentProvince = spouseCurrentProvinces.get(0).getProvince_city_name_kh();
            }
            if (!spouseCurrentDistricts.isEmpty()) {
                spouseCurrentDistrict = spouseCurrentDistricts.get(0).getDistrict_name_kh();
            }
            if (!spouseCurrentCommunes.isEmpty()) {
                spouseCurrentCommune = spouseCurrentCommunes.get(0).getCommune_name_kh();
            }
            if (!spouseCurrentVillages.isEmpty()) {
                spouseCurrentVillage = spouseCurrentVillages.get(0).getVillage_name_kh();
            }
            spouseCurrentStreetNumber = employee.getSpouse().getCurrentAddress().getStreetNumber();
            spouseCurrentHouseNumber = employee.getSpouse().getCurrentAddress().getHouseNumber();
        }

//end spouse current address
        //        father current address

        String fatherCurrentProvince = employee.getFather().getCurrentAddress().getProvinces().get(0).getProvince_city_name_kh();
        String fatherCurrentDistrict = employee.getFather().getCurrentAddress().getDistricts().get(0).getDistrict_name_kh();
        String fatherCurrentCommune = employee.getFather().getCurrentAddress().getCommunes().get(0).getCommune_name_kh();
        String fatherCurrentVillage = employee.getFather().getCurrentAddress().getVillages().get(0).getVillage_name_kh();
        String fatherCurrentStreetNumber = employee.getFather().getCurrentAddress().getStreetNumber();
        String fatherCurrentHouseNumber = employee.getFather().getCurrentAddress().getHouseNumber();
//end father current address
        //        mother current address

        String motherCurrentProvince = employee.getMother().getCurrentAddress().getProvinces().get(0).getProvince_city_name_kh();
        String motherCurrentDistrict = employee.getMother().getCurrentAddress().getDistricts().get(0).getDistrict_name_kh();
        String motherCurrentCommune = employee.getMother().getCurrentAddress().getCommunes().get(0).getCommune_name_kh();
        String motherCurrentVillage = employee.getMother().getCurrentAddress().getVillages().get(0).getVillage_name_kh();
        String motherCurrentStreetNumber = employee.getMother().getCurrentAddress().getStreetNumber();
        String motherCurrentHouseNumber = employee.getMother().getCurrentAddress().getHouseNumber();
//end mother current address

//        emp current address

        String updateDateJoinGov = KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getDateJoinGov()));
        String updateDateJoinPolice = KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getDateJoinPolice()));
        String updateStartYear = KhmerNumberUtil.convertToKhmerNumber(employee.getPreviousActivityAndPositionStartYear());
        // Parameters map can be used to pass additional parameters to the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_PATH", REPORT_DIR);
        parameters.put("employeeKhmerDOB", KhmerNumberUtil.convertToKhmerDayMonthYear(employee.getFormattedDateOfBirth()));
        parameters.put("VOCATIONAL_TRAINING", vocationalTrainingsDatasource);
        parameters.put("APPRECIATION", appreciationDataset);
        parameters.put("ACTIVITY", activityDataset);
        parameters.put("WEAPON_LIST", empWeapons);
        parameters.put("VEHICLE_LIST", empVehicle);
        parameters.put("DEGREE_LEVEL_LIST", employeeDegreeLevels);
        parameters.put("empProvince", empProvince);
        parameters.put("empDistrict", empDistrict);
        parameters.put("empCommune", empCommune);
        parameters.put("empVillage", empVillage);
        parameters.put("spouseProvince", spouseProvince);
        parameters.put("spouseDistrict", spouseDistrict);
        parameters.put("spouseCommune", spouseCommune);
        parameters.put("spouseVillage", spouseVillage);
        parameters.put("fatherProvince", fatherProvince);
        parameters.put("fatherDistrict", fatherDistrict);
        parameters.put("fatherCommune", fatherCommune);
        parameters.put("fatherVillage", fatherVillage);
        parameters.put("motherProvince", motherProvince);
        parameters.put("motherDistrict", motherDistrict);
        parameters.put("motherCommune", motherCommune);
        parameters.put("motherVillage", motherVillage);
        parameters.put("empPoliceDocumentKH", KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getPoliceRankDocumentIssueDate())));
        parameters.put("empPositionKH", KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getPositionDocumentIssueDate())));
        parameters.put("empCurrentProvince", empCurrentProvince);
        parameters.put("empCurrentDistrict", empCurrentDistrict);
        parameters.put("empCurrentCommune", empCurrentCommune);
        parameters.put("empCurrentVillage", empCurrentVillage);
        parameters.put("empCurrentStreetNumber", empCurrentStreetNumber);
        parameters.put("empCurrentHouseNumber", empCurrentHouseNumber);
        parameters.put("spouseCurrentProvince", spouseCurrentProvince);
        parameters.put("spouseCurrentDistrict", spouseCurrentDistrict);
        parameters.put("spouseCurrentCommune", spouseCurrentCommune);
        parameters.put("spouseCurrentVillage", spouseCurrentVillage);
        parameters.put("spouseCurrentStreetNumber", spouseCurrentStreetNumber);
        parameters.put("spouseCurrentHouseNumber", spouseCurrentHouseNumber);
        parameters.put("fatherCurrentProvince", fatherCurrentProvince);
        parameters.put("fatherCurrentDistrict", fatherCurrentDistrict);
        parameters.put("fatherCurrentCommune", fatherCurrentCommune);
        parameters.put("fatherCurrentVillage", fatherCurrentVillage);
        parameters.put("fatherCurrentStreetNumber", fatherCurrentStreetNumber);
        parameters.put("fatherCurrentHouseNumber", fatherCurrentHouseNumber);
        parameters.put("motherCurrentProvince", motherCurrentProvince);
        parameters.put("motherCurrentDistrict", motherCurrentDistrict);
        parameters.put("motherCurrentCommune", motherCurrentCommune);
        parameters.put("motherCurrentVillage", motherCurrentVillage);
        parameters.put("motherCurrentStreetNumber", motherCurrentStreetNumber);
        parameters.put("motherCurrentHouseNumber", motherCurrentHouseNumber);
        parameters.put("IMAGE_NAME", imageName);
        parameters.put("UNIVERSITY_SKILL", universitySkill);
        parameters.put("FOREIGN_LANGUAGE", foreignLanguage);
        parameters.put("SPOUSE_DOB", spouseDOB);
        parameters.put("FATHER_DOB", fatherDOB);
        parameters.put("MOTHER_DOB", motherDOB);
        parameters.put("CHILD_NUMBER", childNumber);
        parameters.put("CHILDREN_LIST", childrenList);
        parameters.put("TOTAL_FEMALE_CHILD", totalFemaleChildren);
        parameters.put("TOTAL_MALE_CHILD", totalMaleChildren);
        parameters.put("KHMER_LUNAR_DATE", khmerLunarDateString);
        parameters.put("KHMER_YEAR_STRING", khmerYearString);
        parameters.put("DATE_JOIN_POLICE", updateDateJoinPolice);
        parameters.put("DATE_JOIN_GOV", updateDateJoinGov);
        parameters.put("START_YEAR", updateStartYear);
        // Fill the report
        JasperPrint jasperPrint;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        } catch (JRException e) {
            throw new JRException("Unable to fill the report with data.", e);
        }

        // Export the report based on the format
        String filePath = "employee_report." + reportFormat;
        try {
            if (reportFormat.equalsIgnoreCase("html")) {
                JasperExportManager.exportReportToHtmlFile(jasperPrint, filePath);
            } else if (reportFormat.equalsIgnoreCase("pdf")) {
                JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);
            } else {
                throw new RuntimeException("Unknown report format: " + reportFormat);
            }
        } catch (JRException e) {
            throw new JRException("Unable to export the report to the specified format: " + reportFormat, e);
        }

        return "Report generated in " + reportFormat + " format at: " + filePath;
    }

    public String formatDateToKh(LocalDate localDate) {
        if (localDate != null) {
            return localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
        return "";
    }

    public List<DegreeLevelInfoDTO> extractDegreeLevels(Employee employee) {
        Set<EmployeeDegreeLevel> employeeDegreeLevels = employee.getEmployeeDegreeLevels();
        List<DegreeLevelInfoDTO> degreeLevelDTOList = new ArrayList<>();

        for (EmployeeDegreeLevel degreeLevel : employeeDegreeLevels) {
            int id = degreeLevel.getDegreeLevel().getId();
            boolean isChecked = degreeLevel.getIsChecked();
            String educationLevel = degreeLevel.getDegreeLevel().getEducationLevel();

            // Create a DTO object and add to the list
            DegreeLevelInfoDTO dto = new DegreeLevelInfoDTO(id, isChecked, educationLevel);
            degreeLevelDTOList.add(dto);
        }
        // Sort the list by id using Comparator
        degreeLevelDTOList.sort(Comparator.comparingInt(DegreeLevelInfoDTO::getId));
        return degreeLevelDTOList;
    }

    private int getNumberOfChildren(Employee e) {
        if (e != null && e.getSpouse() != null) {
            if (e.getSpouse().getChildren() != null) {
                return e.getSpouse().getChildren().size();
            }
        }
        return 0;
    }

    private Set<SpouseChildren> getChildrenList(Employee e) {
        if (e.getSpouse() == null) return Set.of();
        return e.getSpouse().getChildren() != null ? e.getSpouse().getChildren() : Set.of();
    }

    public int countChildrenByGender(String gender, Employee e) {
        if (e.getSpouse() == null) return 0;
        if (e.getSpouse().getChildren() == null) return 0;
        return (int) e.getSpouse().getChildren().stream()
                .filter(child -> gender.equalsIgnoreCase(child.getGender()))
                .count();
    }

    public List<VTTrainingPDFDTO> updateVocationalTrainingKH(List<VocationalTraining> vocationalTrainingList) {
        if (vocationalTrainingList == null || vocationalTrainingList.isEmpty()) {
            return null;
        }

        List<VTTrainingPDFDTO> list = new ArrayList<>();
        for (VocationalTraining vocationalTraining : vocationalTrainingList) {
            try {
                VTTrainingPDFDTO dto = new VTTrainingPDFDTO();
                dto.setTrainingStartDate(KhmerNumberUtil.convertToKhmerDayMonthYearAndOnlyYear(formatDateToKh(vocationalTraining.getTrainingStartDate()), vocationalTraining.getIsNoStartDayMonth()));
                dto.setTrainingToDate(KhmerNumberUtil.convertToKhmerDayMonthYearAndOnlyYear(formatDateToKh(vocationalTraining.getTrainingToDate()), vocationalTraining.getIsNoEndDayMonth()));
                dto.setTrainingDuration(KhmerNumberUtil.convertToKhmerNumber(vocationalTraining.getTrainingDuration()));
                dto.setTrainingCourse(vocationalTraining.getTrainingCourse());
                dto.setTrainingCenter(vocationalTraining.getTrainingCenter());
                dto.setIsNoStartDayMonth(vocationalTraining.getIsNoStartDayMonth());
                dto.setIsNoEndDayMonth(vocationalTraining.getIsNoEndDayMonth());
                dto.setRealTrainingStartDate(vocationalTraining.getTrainingStartDate());
                list.add(dto);
            } catch (Exception e) {
                System.err.println("Error processing vocational training entry: " + e.getMessage());
            }
        }
//        list.sort(Comparator.comparing(VTTrainingPDFDTO::getRealTrainingStartDate));
        list.sort(Comparator.nullsLast(Comparator.comparing(VTTrainingPDFDTO::getRealTrainingStartDate, Comparator.nullsLast(Comparator.naturalOrder()))));
        return list;
    }

    public List<AppreciationPDFDTO> updateAppreciationKH(List<Appreciation> appreciationList) {

        if (appreciationList == null || appreciationList.isEmpty()) {
            return List.of(new AppreciationPDFDTO());
        }

        List<AppreciationPDFDTO> list = new ArrayList<>();
        for (Appreciation appreciation : appreciationList) {
            try {
                AppreciationPDFDTO dto = new AppreciationPDFDTO();
                dto.setAppreciationDate(KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(appreciation.getAppreciationDate())));
                dto.setAppreciationNumber(appreciation.getAppreciationNumber());
                dto.setAppreciation(appreciation.getAppreciation());
                dto.setRealAppreciationDate(appreciation.getAppreciationDate());

                list.add(dto);
            } catch (Exception e) {
                System.err.println("Error processing vocational training entry: " + e.getMessage());
            }
        }

//        list.sort(Comparator.comparing(AppreciationPDFDTO::getRealAppreciationDate));
        list.sort(Comparator.nullsLast(Comparator.comparing(AppreciationPDFDTO::getRealAppreciationDate, Comparator.nullsLast(Comparator.naturalOrder()))));
        return list;
    }

    public List<PrevActivityPDFDTO> updateActivityKH(List<PreviousActivityAndPosition> activityList) {
        if (activityList == null || activityList.isEmpty()) {
            return null;
        }
// Sort the list by fromDate in ascending order
        activityList.sort(Comparator.comparing(PreviousActivityAndPosition::getFromDate));
        List<PrevActivityPDFDTO> list = new ArrayList<>();
        for (PreviousActivityAndPosition activity : activityList) {
            try {
                PrevActivityPDFDTO dto = new PrevActivityPDFDTO();
                dto.setFromDate(KhmerNumberUtil.convertToKhmerDayMonthYearAndOnlyYear(formatDateToKh(activity.getFromDate()), activity.getIsNoStartDayMonth()));
                dto.setToDate(KhmerNumberUtil.convertToKhmerDayMonthYearAndOnlyYear(formatDateToKh(activity.getToDate()), activity.getIsNoEndDayMonth()));
                dto.setActivityAndRank(activity.getActivityAndRank());
                dto.setDepartmentOrUnit(activity.getDepartmentOrUnit());
                dto.setIsNoStartDayMonth(activity.getIsNoStartDayMonth());
                dto.setIsNoEndDayMonth(activity.getIsNoEndDayMonth());
                list.add(dto);
            } catch (Exception e) {
                System.err.println("Error processing vocational training entry: " + e.getMessage());
            }
        }

        return list;
    }

    public Set<ChildrenPDFDTO> updateChildrenList(Set<SpouseChildren> childrenList) {
        if (childrenList == null || childrenList.isEmpty()) {
            return null;
        }

        // Create a List to enable sorting
        List<ChildrenPDFDTO> list = new ArrayList<>();

        for (SpouseChildren spouseChildren : childrenList) {
            try {
                ChildrenPDFDTO dto = new ChildrenPDFDTO();
                dto.setRealDateOfBirth(spouseChildren.getDateOfBirth());
                dto.setDateOfBirth(KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(spouseChildren.getDateOfBirth())));
                dto.setGender(spouseChildren.getGender());
                dto.setFullName(spouseChildren.getFullName());
                dto.setJob(spouseChildren.getJob());
                list.add(dto);
            } catch (Exception e) {
                System.err.println("Error processing children entry: " + e.getMessage());
            }
        }
        // Sort the list by id in ascending order
        list.sort(Comparator.comparing(ChildrenPDFDTO::getRealDateOfBirth));
        // Convert back to LinkedHashSet to maintain the sorted order in a Set
        return new LinkedHashSet<>(list);
    }

    public ResponseEntity<byte[]> exportReportToFrontEnd(String reportFormat, Integer empId) throws FileNotFoundException, JRException {
        KhmerLunarDate khmerLunarDate = Chhankitek.toKhmerLunarDateFormat(LocalDateTime.now());
        String khmerYearString = ChhankitekUtils.convertIntegerToKhmerNumber(LocalDateTime.now().getYear());
        String khmerLunarDateString = "ឆ្នាំ" + khmerLunarDate.getLunarZodiac() + " " + khmerLunarDate.getLunarEra() + " ព.ស." + khmerLunarDate.getLunarYear();
        Employee employee = employeeRepo.findEmployeeAndUserById(empId);
        employee.setPoliceId(KhmerNumberUtil.convertToKhmerNumber(Integer.parseInt(employee.getPoliceId())));
        employee.setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getPhoneNumber()));


        if (employee.getSpouse() != null)
            employee.getSpouse().setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getSpouse().getPhoneNumber()));
        if (employee.getFather() != null)
            employee.getFather().setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getFather().getPhoneNumber()));
        if (employee.getMother() != null)
            employee.getMother().setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getMother().getPhoneNumber()));
        String childNumber = KhmerNumberUtil.convertToKhmerNumber(getNumberOfChildren(employee));
        String imageName = employee.getUser().getImageName();

        // Load file and compile
        JasperReport jasperReport;
        String reportPath = REPORT_DIR + "employee_report.jrxml";
        Resource resource = resourceLoader.getResource(reportPath);
        try (InputStream inputStream = resource.getInputStream()) {
            jasperReport = JasperCompileManager.compileReport(inputStream);
        } catch (IOException e) {
            throw new FileNotFoundException("The report file was not found: " + reportPath);
        } catch (JRException e) {
            String errorMsg = "Unable to compile the report file: " + reportPath;
            System.err.println(errorMsg);
            throw new JRException(errorMsg, e);
        }

        // Create a list containing the single employee
        List<Employee> employees = Collections.singletonList(employee);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
        List<VTTrainingPDFDTO> vocationalTrainings = updateVocationalTrainingKH(employee.getVocationalTrainings());
        JRBeanCollectionDataSource vocationalTrainingsDatasource = new JRBeanCollectionDataSource(vocationalTrainings);
        List<AppreciationPDFDTO> appreciations = updateAppreciationKH(employee.getAppreciations());
        JRBeanCollectionDataSource appreciationDataset = new JRBeanCollectionDataSource(appreciations);
        List<PrevActivityPDFDTO> previousActivityAndPositions = updateActivityKH(employee.getActivityAndPositions());
        JRBeanCollectionDataSource activityDataset = new JRBeanCollectionDataSource(previousActivityAndPositions);
        List<DegreeLevelInfoDTO> employeeDegreeLevels = extractDegreeLevels(employee);
        List<Weapon> empWeapons = employee.getWeapons();
        List<PolicePlateNumberCar> empVehicle = employee.getPolicePlateNumberCars();
        Set<ChildrenPDFDTO> childrenList = updateChildrenList(getChildrenList(employee));
        String universitySkill = EmployeeUtil.updateUniversitySkill(employee);
        String foreignLanguage = EmployeeUtil.updateForeignLanguage(employee);
        String spouseDOB = employee.getSpouse() == null ? "" : KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getSpouse().getDateOfBirth()));
        String fatherDOB = employee.getFather() == null ? "" : KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getFather().getDateOfBirth()));
        String motherDOB = employee.getMother() == null ? "" : KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getMother().getDateOfBirth()));
//        emp place of birth
        String empProvince = employee.getPlaceOfBirth().getProvinces().get(0).getProvince_city_name_kh();
        String empDistrict = employee.getPlaceOfBirth().getDistricts().get(0).getDistrict_name_kh();
        String empCommune = employee.getPlaceOfBirth().getCommunes().get(0).getCommune_name_kh();
        String empVillage = employee.getPlaceOfBirth().getVillages().get(0).getVillage_name_kh();
//end emp place of birth

        //        spouse place of birth
        String spouseProvince = "";
        String spouseDistrict = "";
        String spouseCommune = "";
        String spouseVillage = "";

        if (employee.getSpouse() != null && employee.getSpouse().getPlaceOfBirth() != null) {
            List<ProvinceCity> spouseProvinces = employee.getSpouse().getPlaceOfBirth().getProvinces();
            List<District> spouseDistricts = employee.getSpouse().getPlaceOfBirth().getDistricts();
            List<Commune> spouseCommunes = employee.getSpouse().getPlaceOfBirth().getCommunes();
            List<Village> spouseVillages = employee.getSpouse().getPlaceOfBirth().getVillages();

            if (!spouseProvinces.isEmpty()) {
                spouseProvince = spouseProvinces.get(0).getProvince_city_name_kh();
            }
            if (!spouseDistricts.isEmpty()) {
                spouseDistrict = spouseDistricts.get(0).getDistrict_name_kh();
            }
            if (!spouseCommunes.isEmpty()) {
                spouseCommune = spouseCommunes.get(0).getCommune_name_kh();
            }
            if (!spouseVillages.isEmpty()) {
                spouseVillage = spouseVillages.get(0).getVillage_name_kh();
            }
        }

//end spouse place of birth
        //        father place of birth
        String fatherProvince = "";
        String fatherDistrict = "";
        String fatherCommune = "";
        String fatherVillage = "";

        if (employee.getFather() != null && employee.getFather().getPlaceOfBirth() != null) {
            List<ProvinceCity> fatherProvinces = employee.getFather().getPlaceOfBirth().getProvinces();
            List<District> fatherDistricts = employee.getFather().getPlaceOfBirth().getDistricts();
            List<Commune> fatherCommunes = employee.getFather().getPlaceOfBirth().getCommunes();
            List<Village> fatherVillages = employee.getFather().getPlaceOfBirth().getVillages();

            if (!fatherProvinces.isEmpty()) {
                fatherProvince = fatherProvinces.get(0).getProvince_city_name_kh();
            }
            if (!fatherDistricts.isEmpty()) {
                fatherDistrict = fatherDistricts.get(0).getDistrict_name_kh();
            }
            if (!fatherCommunes.isEmpty()) {
                fatherCommune = fatherCommunes.get(0).getCommune_name_kh();
            }
            if (!fatherVillages.isEmpty()) {
                fatherVillage = fatherVillages.get(0).getVillage_name_kh();
            }
        }
//end father place of birth
        //        mother place of birth
        String motherProvince = "";
        String motherDistrict = "";
        String motherCommune = "";
        String motherVillage = "";

        if (employee.getMother() != null && employee.getMother().getPlaceOfBirth() != null) {
            List<ProvinceCity> motherProvinces = employee.getMother().getPlaceOfBirth().getProvinces();
            List<District> motherDistricts = employee.getMother().getPlaceOfBirth().getDistricts();
            List<Commune> motherCommunes = employee.getMother().getPlaceOfBirth().getCommunes();
            List<Village> motherVillages = employee.getMother().getPlaceOfBirth().getVillages();

            if (!motherProvinces.isEmpty()) {
                motherProvince = motherProvinces.get(0).getProvince_city_name_kh();
            }
            if (!motherDistricts.isEmpty()) {
                motherDistrict = motherDistricts.get(0).getDistrict_name_kh();
            }
            if (!motherCommunes.isEmpty()) {
                motherCommune = motherCommunes.get(0).getCommune_name_kh();
            }
            if (!motherVillages.isEmpty()) {
                motherVillage = motherVillages.get(0).getVillage_name_kh();
            }
        }
//end mother place of birth
//        emp current address

        String empCurrentProvince = employee.getCurrentAddress().getProvinces().get(0).getProvince_city_name_kh();
        String empCurrentDistrict = employee.getCurrentAddress().getDistricts().get(0).getDistrict_name_kh();
        String empCurrentCommune = employee.getCurrentAddress().getCommunes().get(0).getCommune_name_kh();
        String empCurrentVillage = employee.getCurrentAddress().getVillages().get(0).getVillage_name_kh();
        String empCurrentStreetNumber = employee.getCurrentAddress().getStreetNumber();
        String empCurrentHouseNumber = employee.getCurrentAddress().getHouseNumber();
//end emp current address
//        spouse current address

        String spouseCurrentProvince = "";
        String spouseCurrentDistrict = "";
        String spouseCurrentCommune = "";
        String spouseCurrentVillage = "";
        String spouseCurrentStreetNumber = "";
        String spouseCurrentHouseNumber = "";
        String totalFemaleChildren = KhmerNumberUtil.convertToKhmerNumber(countChildrenByGender("F", employee));
        String totalMaleChildren = KhmerNumberUtil.convertToKhmerNumber(countChildrenByGender("M", employee));
        if (employee.getSpouse() != null && employee.getSpouse().getCurrentAddress() != null) {
            List<ProvinceCity> spouseCurrentProvinces = employee.getSpouse().getCurrentAddress().getProvinces();
            List<District> spouseCurrentDistricts = employee.getSpouse().getCurrentAddress().getDistricts();
            List<Commune> spouseCurrentCommunes = employee.getSpouse().getCurrentAddress().getCommunes();
            List<Village> spouseCurrentVillages = employee.getSpouse().getCurrentAddress().getVillages();

            if (!spouseCurrentProvinces.isEmpty()) {
                spouseCurrentProvince = spouseCurrentProvinces.get(0).getProvince_city_name_kh();
            }
            if (!spouseCurrentDistricts.isEmpty()) {
                spouseCurrentDistrict = spouseCurrentDistricts.get(0).getDistrict_name_kh();
            }
            if (!spouseCurrentCommunes.isEmpty()) {
                spouseCurrentCommune = spouseCurrentCommunes.get(0).getCommune_name_kh();
            }
            if (!spouseCurrentVillages.isEmpty()) {
                spouseCurrentVillage = spouseCurrentVillages.get(0).getVillage_name_kh();
            }
            spouseCurrentStreetNumber = employee.getSpouse().getCurrentAddress().getStreetNumber();
            spouseCurrentHouseNumber = employee.getSpouse().getCurrentAddress().getHouseNumber();
        }

//end spouse current address
        //        father current address

        String fatherCurrentProvince = "";
        String fatherCurrentDistrict = "";
        String fatherCurrentCommune = "";
        String fatherCurrentVillage = "";
        String fatherCurrentStreetNumber = "";
        String fatherCurrentHouseNumber = "";
        if (employee.getFather() != null && employee.getFather().getCurrentAddress() != null) {
            List<ProvinceCity> fatherCurrentProvinces = employee.getFather().getCurrentAddress().getProvinces();
            List<District> fatherCurrentDistricts = employee.getFather().getCurrentAddress().getDistricts();
            List<Commune> fatherCurrentCommunes = employee.getFather().getCurrentAddress().getCommunes();
            List<Village> fatherCurrentVillages = employee.getFather().getCurrentAddress().getVillages();

            if (!fatherCurrentProvinces.isEmpty()) {
                fatherCurrentProvince = fatherCurrentProvinces.get(0).getProvince_city_name_kh();
            }
            if (!fatherCurrentDistricts.isEmpty()) {
                fatherCurrentDistrict = fatherCurrentDistricts.get(0).getDistrict_name_kh();
            }
            if (!fatherCurrentCommunes.isEmpty()) {
                fatherCurrentCommune = fatherCurrentCommunes.get(0).getCommune_name_kh();
            }
            if (!fatherCurrentVillages.isEmpty()) {
                fatherCurrentVillage = fatherCurrentVillages.get(0).getVillage_name_kh();
            }
            fatherCurrentStreetNumber = employee.getFather().getCurrentAddress().getStreetNumber();
            fatherCurrentHouseNumber = employee.getFather().getCurrentAddress().getHouseNumber();
        }
//end father current address
        //        mother current address

        String motherCurrentProvince = "";
        String motherCurrentDistrict = "";
        String motherCurrentCommune = "";
        String motherCurrentVillage = "";
        String motherCurrentStreetNumber = "";
        String motherCurrentHouseNumber = "";
        if (employee.getMother() != null && employee.getMother().getCurrentAddress() != null) {
            List<ProvinceCity> motherCurrentProvinces = employee.getMother().getCurrentAddress().getProvinces();
            List<District> motherCurrentDistricts = employee.getMother().getCurrentAddress().getDistricts();
            List<Commune> motherCurrentCommunes = employee.getMother().getCurrentAddress().getCommunes();
            List<Village> motherCurrentVillages = employee.getMother().getCurrentAddress().getVillages();

            if (!motherCurrentProvinces.isEmpty()) {
                motherCurrentProvince = motherCurrentProvinces.get(0).getProvince_city_name_kh();
            }
            if (!motherCurrentDistricts.isEmpty()) {
                motherCurrentDistrict = motherCurrentDistricts.get(0).getDistrict_name_kh();
            }
            if (!motherCurrentCommunes.isEmpty()) {
                motherCurrentCommune = motherCurrentCommunes.get(0).getCommune_name_kh();
            }
            if (!motherCurrentVillages.isEmpty()) {
                motherCurrentVillage = motherCurrentVillages.get(0).getVillage_name_kh();
            }
            motherCurrentStreetNumber = employee.getMother().getCurrentAddress().getStreetNumber();
            motherCurrentHouseNumber = employee.getMother().getCurrentAddress().getHouseNumber();
        }
//end mother current address

//        emp current address

        String updateDateJoinGov = KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getDateJoinGov()));
        String updateDateJoinPolice = KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getDateJoinPolice()));
        String updateStartYear = KhmerNumberUtil.convertToKhmerNumber(employee.getPreviousActivityAndPositionStartYear());
        // Parameters map can be used to pass additional parameters to the report
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("REPORT_PATH", REPORT_DIR);
        parameters.put("REPORT_PHOTO_PATH", REPORT_PHOTO_PATH);
        parameters.put("employeeKhmerDOB", KhmerNumberUtil.convertToKhmerDayMonthYear(employee.getFormattedDateOfBirth()));
        parameters.put("VOCATIONAL_TRAINING", vocationalTrainingsDatasource);
        parameters.put("APPRECIATION", appreciationDataset);
        parameters.put("ACTIVITY", activityDataset);
        parameters.put("WEAPON_LIST", empWeapons);
        parameters.put("VEHICLE_LIST", empVehicle);
        parameters.put("DEGREE_LEVEL_LIST", employeeDegreeLevels);
        parameters.put("empProvince", empProvince);
        parameters.put("empDistrict", empDistrict);
        parameters.put("empCommune", empCommune);
        parameters.put("empVillage", empVillage);
        parameters.put("spouseProvince", spouseProvince);
        parameters.put("spouseDistrict", spouseDistrict);
        parameters.put("spouseCommune", spouseCommune);
        parameters.put("spouseVillage", spouseVillage);
        parameters.put("fatherProvince", fatherProvince);
        parameters.put("fatherDistrict", fatherDistrict);
        parameters.put("fatherCommune", fatherCommune);
        parameters.put("fatherVillage", fatherVillage);
        parameters.put("motherProvince", motherProvince);
        parameters.put("motherDistrict", motherDistrict);
        parameters.put("motherCommune", motherCommune);
        parameters.put("motherVillage", motherVillage);
        parameters.put("empPoliceDocumentKH", KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getPoliceRankDocumentIssueDate())));
        parameters.put("empPositionKH", KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getPositionDocumentIssueDate())));
        parameters.put("empCurrentProvince", empCurrentProvince);
        parameters.put("empCurrentDistrict", empCurrentDistrict);
        parameters.put("empCurrentCommune", empCurrentCommune);
        parameters.put("empCurrentVillage", empCurrentVillage);
        parameters.put("empCurrentStreetNumber", empCurrentStreetNumber);
        parameters.put("empCurrentHouseNumber", empCurrentHouseNumber);
        parameters.put("spouseCurrentProvince", spouseCurrentProvince);
        parameters.put("spouseCurrentDistrict", spouseCurrentDistrict);
        parameters.put("spouseCurrentCommune", spouseCurrentCommune);
        parameters.put("spouseCurrentVillage", spouseCurrentVillage);
        parameters.put("spouseCurrentStreetNumber", spouseCurrentStreetNumber);
        parameters.put("spouseCurrentHouseNumber", spouseCurrentHouseNumber);
        parameters.put("fatherCurrentProvince", fatherCurrentProvince);
        parameters.put("fatherCurrentDistrict", fatherCurrentDistrict);
        parameters.put("fatherCurrentCommune", fatherCurrentCommune);
        parameters.put("fatherCurrentVillage", fatherCurrentVillage);
        parameters.put("fatherCurrentStreetNumber", fatherCurrentStreetNumber);
        parameters.put("fatherCurrentHouseNumber", fatherCurrentHouseNumber);
        parameters.put("motherCurrentProvince", motherCurrentProvince);
        parameters.put("motherCurrentDistrict", motherCurrentDistrict);
        parameters.put("motherCurrentCommune", motherCurrentCommune);
        parameters.put("motherCurrentVillage", motherCurrentVillage);
        parameters.put("motherCurrentStreetNumber", motherCurrentStreetNumber);
        parameters.put("motherCurrentHouseNumber", motherCurrentHouseNumber);
        parameters.put("IMAGE_NAME", imageName);
        parameters.put("UNIVERSITY_SKILL", universitySkill);
        parameters.put("FOREIGN_LANGUAGE", foreignLanguage);
        parameters.put("SPOUSE_DOB", spouseDOB);
        parameters.put("FATHER_DOB", fatherDOB);
        parameters.put("MOTHER_DOB", motherDOB);
        parameters.put("CHILD_NUMBER", childNumber);
        parameters.put("CHILDREN_LIST", childrenList);
        parameters.put("TOTAL_FEMALE_CHILD", totalFemaleChildren);
        parameters.put("TOTAL_MALE_CHILD", totalMaleChildren);
        parameters.put("KHMER_LUNAR_DATE", khmerLunarDateString);
        parameters.put("KHMER_YEAR_STRING", khmerYearString);
        parameters.put("DATE_JOIN_POLICE", updateDateJoinPolice);
        parameters.put("DATE_JOIN_GOV", updateDateJoinGov);
        parameters.put("START_YEAR", updateStartYear);
        // Fill the report
        JasperPrint jasperPrint;
        try {
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        } catch (JRException e) {
            throw new JRException("Unable to fill the report with data.", e);
        }

        // Export the report based on the format
        String filePath = "employee_report." + reportFormat;
        HttpHeaders headers = new HttpHeaders();
        byte[] data = new byte[0];
        try {
            if (reportFormat.equalsIgnoreCase("html")) {
                JasperExportManager.exportReportToHtmlFile(jasperPrint, filePath);
            } else if (reportFormat.equalsIgnoreCase("pdf")) {
//                JasperExportManager.exportReportToPdfFile(jasperPrint, filePath);

                data = JasperExportManager.exportReportToPdf(jasperPrint);

                headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline:filename=employee_report.pdf");
            } else {
                throw new RuntimeException("Unknown report format: " + reportFormat);
            }
        } catch (JRException e) {
            throw new JRException("Unable to export the report to the specified format: " + reportFormat, e);
        }

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }
}
package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.dto.DegreeLevelInfoDTO;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.provinces.District;
import com.noc.employee_cv.provinces.ProvinceCity;
import com.noc.employee_cv.provinces.Village;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.utils.EmployeeUtil;
import com.noc.employee_cv.utils.KhmerNumberUtil;
import com.noc.employee_cv.utils.PhoneNumberFormatter;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImp {

    private final EmployeeRepo employeeRepo;
    @Value("${file.report-dir}")
    private String REPORT_DIR;

    public String exportReport(String reportFormat, Integer empId) throws FileNotFoundException, JRException {
        Employee employee = employeeRepo.findEmployeeAndUserById(empId);
        employee.setPoliceId(KhmerNumberUtil.convertToKhmerNumber(Integer.parseInt(employee.getPoliceId())));
        employee.setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getPhoneNumber()));


        if(employee.getSpouse()!=null)employee.getSpouse().setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getSpouse().getPhoneNumber()));
        if(employee.getFather()!=null)employee.getFather().setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getFather().getPhoneNumber()));
        if(employee.getMother()!=null)employee.getMother().setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getMother().getPhoneNumber()));
        int childNumber= getNumberOfChildren(employee);
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
        List<VocationalTraining> vocationalTrainings = employee.getVocationalTrainings();
        JRBeanCollectionDataSource vocationalTrainingsDatasource = new JRBeanCollectionDataSource(vocationalTrainings);
        List<Appreciation> appreciations = employee.getAppreciations();
        JRBeanCollectionDataSource appreciationDataset = new JRBeanCollectionDataSource(appreciations);
        List<PreviousActivityAndPosition> previousActivityAndPositions = employee.getActivityAndPositions();
        JRBeanCollectionDataSource activityDataset = new JRBeanCollectionDataSource(previousActivityAndPositions);

        List<DegreeLevelInfoDTO> employeeDegreeLevels = extractDegreeLevels(employee);
        List<Weapon> empWeapons = employee.getWeapons();
        List<PolicePlateNumberCar> empVehicle = employee.getPolicePlateNumberCars();
        Set<SpouseChildren> childrenList=getChildrenList(employee);
        String universitySkill = EmployeeUtil.updateUniversitySkill(employee);
        String foreignLanguage = EmployeeUtil.updateForeignLanguage(employee);
        String spouseDOB =employee.getSpouse()==null?"": KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getSpouse().getDateOfBirth()));
        String fatherDOB = employee.getFather()==null?"": KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getFather().getDateOfBirth()));
        String motherDOB = employee.getMother()==null?"": KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getMother().getDateOfBirth()));
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
        long totalFemaleChildren=countChildrenByGender("M",employee);
        long totalMaleChildren =countChildrenByGender("F",employee);
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
    public long countChildrenByGender(String gender,Employee e) {
        if(e.getSpouse().getChildren()==null) return 0;
        return e.getSpouse().getChildren().stream()
                .filter(child -> gender.equalsIgnoreCase(child.getGender()))
                .count();
    }


}
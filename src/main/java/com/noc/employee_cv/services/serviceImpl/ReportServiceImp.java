package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.dto.DegreeLevelInfoDTO;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.repositories.EmployeeRepo;
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
        Employee employee = employeeRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + empId));
        employee.setPoliceId(KhmerNumberUtil.convertToKhmerNumber(Integer.parseInt(employee.getPoliceId())));
        employee.setPhoneNumber(PhoneNumberFormatter.updatePhoneNumber(employee.getPhoneNumber()));

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
        JRBeanCollectionDataSource vocationalTrainingsDatasource= new JRBeanCollectionDataSource(vocationalTrainings);
        List<Appreciation>  appreciations= employee.getAppreciations();
        JRBeanCollectionDataSource appreciationDataset= new JRBeanCollectionDataSource(appreciations);
        List<PreviousActivityAndPosition>  previousActivityAndPositions= employee.getActivityAndPositions();
        JRBeanCollectionDataSource activityDataset= new JRBeanCollectionDataSource(previousActivityAndPositions);

        List<DegreeLevelInfoDTO> employeeDegreeLevels= extractDegreeLevels(employee);
        List<Weapon>  empWeapons= employee.getWeapons();
        List<PolicePlateNumberCar>  empVehicle= employee.getPolicePlateNumberCars();

//        emp place of birth
        String empProvince= employee.getPlaceOfBirth().getProvinces().get(0).getProvince_city_name_kh();
        String empDistrict= employee.getPlaceOfBirth().getDistricts().get(0).getDistrict_name_kh();
        String empCommune= employee.getPlaceOfBirth().getCommunes().get(0).getCommune_name_kh();
        String empVillage= employee.getPlaceOfBirth().getVillages().get(0).getVillage_name_kh();
//end emp place of birth
//        emp current address

        String empCurrentProvince= employee.getCurrentAddress().getProvinces().get(0).getProvince_city_name_kh();
        String empCurrentDistrict= employee.getCurrentAddress().getDistricts().get(0).getDistrict_name_kh();
        String empCurrentCommune= employee.getCurrentAddress().getCommunes().get(0).getCommune_name_kh();
        String empCurrentVillage= employee.getCurrentAddress().getVillages().get(0).getVillage_name_kh();
        String empCurrentStreetNumber= employee.getCurrentAddress().getStreetNumber();
        String empCurrentHouseNumber= employee.getCurrentAddress().getHouseNumber();
//end emp current address
//        emp current address
        // Parameters map can be used to pass additional parameters to the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_PATH",REPORT_DIR);
        parameters.put("employeeKhmerDOB",KhmerNumberUtil.convertToKhmerDayMonthYear(employee.getFormattedDateOfBirth()));
        parameters.put("VOCATIONAL_TRAINING",vocationalTrainingsDatasource);
        parameters.put("APPRECIATION",appreciationDataset);
        parameters.put("ACTIVITY",activityDataset);
        parameters.put("WEAPON_LIST",empWeapons);
        parameters.put("VEHICLE_LIST",empVehicle);
        parameters.put("DEGREE_LEVEL_LIST",employeeDegreeLevels);
        parameters.put("empProvince",empProvince);
        parameters.put("empDistrict",empDistrict);
        parameters.put("empCommune",empCommune);
        parameters.put("empVillage",empVillage);
        parameters.put("empPoliceDocumentKH",KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getPoliceRankDocumentIssueDate())));
        parameters.put("empPositionKH",KhmerNumberUtil.convertToKhmerDayMonthYear(formatDateToKh(employee.getPositionDocumentIssueDate())));
        parameters.put("empCurrentProvince",empCurrentProvince);
        parameters.put("empCurrentDistrict",empCurrentDistrict);
        parameters.put("empCurrentCommune",empCurrentCommune);
        parameters.put("empCurrentVillage",empCurrentVillage);
        parameters.put("empCurrentStreetNumber",empCurrentStreetNumber);
        parameters.put("empCurrentHouseNumber",empCurrentHouseNumber);
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
            DegreeLevelInfoDTO dto = new DegreeLevelInfoDTO(id,isChecked, educationLevel);
            degreeLevelDTOList.add(dto);
        }
        // Sort the list by id using Comparator
        degreeLevelDTOList.sort(Comparator.comparingInt(DegreeLevelInfoDTO::getId));
        return degreeLevelDTOList;
    }

}
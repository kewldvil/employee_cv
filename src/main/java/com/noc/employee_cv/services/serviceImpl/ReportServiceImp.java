package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.Appreciation;
import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.PreviousActivityAndPosition;
import com.noc.employee_cv.models.VocationalTraining;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.utils.KhmerNumberUtil;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImp {

    private final EmployeeRepo employeeRepo;

    public String exportReport(String reportFormat, Integer empId) throws FileNotFoundException, JRException {
        Employee employee = employeeRepo.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + empId));
        employee.setPoliceId(KhmerNumberUtil.convertToKhmerNumber(Integer.parseInt(employee.getPoliceId())));

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

        // Parameters map can be used to pass additional parameters to the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("employeeKhmerDOB",KhmerNumberUtil.convertToKhmerDayMonthYear(employee.getFormattedDateOfBirth()));
        parameters.put("VOCATIONAL_TRAINING",vocationalTrainingsDatasource);
        parameters.put("APPRECIATION",appreciationDataset);
        parameters.put("ACTIVITY",activityDataset);
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

}
package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.dto.EmployeeDTO;
import com.noc.employee_cv.dto.PhoneNumberDTO;
import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.mapper.EmployeeMapper;
import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.models.Employee;
import com.noc.employee_cv.models.EmployeeAddress;
import com.noc.employee_cv.models.PhoneNumber;
import com.noc.employee_cv.repositories.AddressRepo;
import com.noc.employee_cv.repositories.EmployeeAddressRepo;
import com.noc.employee_cv.repositories.EmployeeRepo;
import com.noc.employee_cv.repositories.PhoneNumberRepo;
import com.noc.employee_cv.services.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public void createNew(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.fromEmployeeDto(employeeDTO);
        employee = employeeRepo.save(employee);
        //set phone number
        //Set<PhoneNumber> phoneNumbers = new HashSet<>();
//        for (PhoneNumberDTO dto : employeeDTO.getPhoneNumberList()) {
//            PhoneNumber phoneNumber = new PhoneNumber();
//            phoneNumber.setPhoneNumber(dto.getPhoneNumber());
//            phoneNumber.setEmployee(employee);
//            phoneNumberRepo.save(phoneNumber);
//           // phoneNumbers.add(phoneNumber);
//        }
        System.out.println("Chhaya: " + employee.getId());
        employee.getPhoneNumberList().forEach(phone -> System.out.println(phone.getPhoneNumber()));
    }

    @Override
    @Transactional
    public void saveEmployeeAddress(int employeeId, int addressId, AddressType addressType) {
        Employee employee = employeeRepo.findById(employeeId).orElseThrow();
        Address address = addressRepo.findById(addressId).orElseThrow();

        EmployeeAddress employeeAddress= new EmployeeAddress();
        employeeAddress.setEmployee(employee);
        employeeAddress.setAddress(address);
        employeeAddress.setAddressType(addressType);

        employee.getEmployeeAddresses().add(employeeAddress);
        address.getEmployeeAddresses().add(employeeAddress);

        employeeAddressRepo.save(employeeAddress);
    }

    @Override
    public void save(Employee employee) {
        employeeRepo.save(employee);
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

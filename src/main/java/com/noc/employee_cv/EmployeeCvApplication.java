package com.noc.employee_cv;

import com.noc.employee_cv.enums.AddressType;
import com.noc.employee_cv.enums.BloodType;
import com.noc.employee_cv.models.*;
import com.noc.employee_cv.repositories.EmployeeRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class EmployeeCvApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeCvApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(EmployeeRepo repository) {
//        Set<Address> list = new HashSet<Address>();
//
//        ProvinceCity pc = new ProvinceCity();
//        pc.setProvinceCityCode("123");
//        pc.setProvinceCityNameKh("กรุงเทพมหานคร");
//        pc.setProvinceCityNameEn("Bangkok");
//        District ds = new District();
//        ds.setDistrictCode("123");
//        ds.setDistrictNameKh("กรุงเทพมหานคร");
//        ds.setDistrictNameEn("Bangkok");
//        Commune cm = new Commune();
//        cm.setCommuneCode("123");
//        cm.setCommuneNameKh("กรุงเทพมหานคร");
//        cm.setCommuneNameEn("Bangkok");
//        Village vl = new Village();
//        vl.setVillageCode("123");
//        vl.setVillageNameKh("กรุงเทพมหานคร");
//        vl.setVillageNameEn("Bangkok");
//        Address address1 = new Address(new Employee(),null,pc,ds,cm,vl,AddressType.FATHER_ADDRESS.toString());
//
//        //Address address2 = new Address(null,null,pc,ds,cm,vl,AddressType.FATHER_ADDRESS.toString());
//        //list.add(address2);
//        list.add(address1);
//
//        System.out.println(list.toString());
//        return arg -> {
//            var employee = Employee.builder()
//                    .firstName("nem")
//                    .lastName("sopjhgjhheak")
//                    .bloodType(BloodType.A_NEGATIVE.bloodType)
//                    .employeeAddress(list)
//                    .build();
//            repository.save(employee);
//        };
//    }
}

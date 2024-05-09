package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.AddressDistrict;
import com.noc.employee_cv.provinces.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressDistrictRepo extends JpaRepository<AddressDistrict,Integer> {
}

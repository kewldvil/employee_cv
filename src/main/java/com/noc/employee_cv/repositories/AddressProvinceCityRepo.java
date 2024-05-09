package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.AddressProvinceCity;
import com.noc.employee_cv.provinces.ProvinceCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressProvinceCityRepo extends JpaRepository<AddressProvinceCity,Integer> {
}

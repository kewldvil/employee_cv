package com.noc.employee_cv.repositories;

import com.noc.employee_cv.provinces.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepo extends JpaRepository<District,Integer> {
    @Query("SELECT d FROM District d WHERE d.province_city_id = :provinceId")
    List<District> findByProvince_city_id(Integer provinceId);
}

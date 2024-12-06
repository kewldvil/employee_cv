package com.noc.employee_cv.repositories;

import com.noc.employee_cv.provinces.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepo extends JpaRepository<District,Integer> {

    @Query("SELECT d FROM District d WHERE d.province_city_id = :provinceId AND d.enabled = true")
    List<District> findByProvinceCityIdAndEnabledTrue(Integer provinceId);
    @Query(value = "SELECT * FROM District WHERE district_code IS NOT NULL ORDER BY district_code DESC LIMIT 1", nativeQuery = true)
    Optional<District> findFirstByOrderByDistrict_codeDesc();



}

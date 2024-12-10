package com.noc.employee_cv.repositories;

import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.provinces.District;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommuneRepo extends JpaRepository<Commune,Integer> {
    @Query("SELECT c FROM Commune c WHERE c.district_id = :id AND c.enabled=true")
    List<Commune> findByDistrictIdEAndEnabledTrue(Integer id);

    @Query(value = "SELECT * FROM Commune WHERE commune_code IS NOT NULL ORDER BY commune_code DESC LIMIT 1", nativeQuery = true)
    Optional<Commune> findFirstByOrderByCommuneCodeDesc();

    @Modifying
    @Transactional
    @Query("UPDATE Commune c SET c.enabled = false WHERE c.district_id = :districtId")
    int updateSetEnabledFalseWhereDistrictId(@Param("districtId") int districtId);

    @Query("SELECT c FROM Commune c WHERE c.district_id = :districtId AND c.enabled = false")
    List<Commune> findDisabledCommunesByDistrictId(@Param("districtId") int districtId);
}



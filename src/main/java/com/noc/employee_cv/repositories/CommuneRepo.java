package com.noc.employee_cv.repositories;

import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.provinces.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommuneRepo extends JpaRepository<Commune,Integer> {
    @Query("SELECT c FROM Commune c WHERE c.district_id = :id AND c.enabled=true")
    List<Commune> findByDistrictIdEAndEnabledTrue(Integer id);
    @Query(value = "SELECT * FROM Commune ORDER BY commune_code DESC LIMIT 1", nativeQuery = true)
    Optional<Commune> findFirstByOrderByCommuneCodeDesc();
}

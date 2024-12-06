package com.noc.employee_cv.repositories;

import com.noc.employee_cv.provinces.District;
import com.noc.employee_cv.provinces.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VillageRepo extends JpaRepository<Village,Integer> {
    @Query("SELECT v FROM Village v WHERE v.commune_id = :id AND v.enabled=true")
    List<Village> findByCommuneIdAndEnabledTrue(Integer id);
    @Query(value = "SELECT * FROM Village WHERE village_code IS NOT NULL ORDER BY village_code DESC LIMIT 1", nativeQuery = true)
    Optional<Village> findFirstByOrderByVillageCodeDesc();
}

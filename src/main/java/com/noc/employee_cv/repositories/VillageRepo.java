package com.noc.employee_cv.repositories;

import com.noc.employee_cv.provinces.District;
import com.noc.employee_cv.provinces.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VillageRepo extends JpaRepository<Village,Integer> {
    @Query("SELECT v FROM Village v WHERE v.commune_id = :id")
    List<Village> findByCommune_id(Integer id);
}

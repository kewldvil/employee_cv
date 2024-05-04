package com.noc.employee_cv.repositories;

import com.noc.employee_cv.provinces.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommuneRepo extends JpaRepository<Commune,Integer> {
    @Query("SELECT c FROM Commune c WHERE c.district_id = :id")
    List<Commune> findByDistrict_id(Integer id);
}

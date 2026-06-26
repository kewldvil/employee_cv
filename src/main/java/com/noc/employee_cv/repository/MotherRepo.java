package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.Mother;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotherRepo extends JpaRepository<Mother,Integer> {
}

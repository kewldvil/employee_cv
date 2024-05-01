package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Mother;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotherRepo extends JpaRepository<Mother,Integer> {
}

package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.Father;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FatherRepo extends JpaRepository<Father,Integer> {
}

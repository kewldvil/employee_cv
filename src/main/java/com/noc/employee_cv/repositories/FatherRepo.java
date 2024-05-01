package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Father;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FatherRepo extends JpaRepository<Father,Integer> {
}

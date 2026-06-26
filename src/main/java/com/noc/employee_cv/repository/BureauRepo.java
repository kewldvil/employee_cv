package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.Bureau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BureauRepo extends JpaRepository<Bureau,Integer> {
}

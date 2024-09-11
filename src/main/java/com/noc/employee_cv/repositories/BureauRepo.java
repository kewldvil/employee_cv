package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Bureau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BureauRepo extends JpaRepository<Bureau,Integer> {
}

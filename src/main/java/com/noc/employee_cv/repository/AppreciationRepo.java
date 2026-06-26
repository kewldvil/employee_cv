package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.Appreciation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppreciationRepo extends JpaRepository<Appreciation,Integer> {
}

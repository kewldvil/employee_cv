package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Appreciation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppreciationRepo extends JpaRepository<Appreciation,Integer> {
}

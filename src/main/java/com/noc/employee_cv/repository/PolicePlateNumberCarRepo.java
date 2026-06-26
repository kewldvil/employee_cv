package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.PolicePlateNumberCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicePlateNumberCarRepo extends JpaRepository<PolicePlateNumberCar,Integer> {
}

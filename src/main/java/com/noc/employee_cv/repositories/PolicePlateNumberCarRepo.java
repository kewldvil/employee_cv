package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.PolicePlateNumberCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicePlateNumberCarRepo extends JpaRepository<PolicePlateNumberCar,Integer> {
}

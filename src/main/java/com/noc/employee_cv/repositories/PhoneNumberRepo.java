package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumberRepo extends JpaRepository<PhoneNumber,Integer> {
}

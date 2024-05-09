package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.Address;
import com.noc.employee_cv.models.AddressVillage;
import com.noc.employee_cv.provinces.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressVillageRepo extends JpaRepository<AddressVillage,Integer> {
}

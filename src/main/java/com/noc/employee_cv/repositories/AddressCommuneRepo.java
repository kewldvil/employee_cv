package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.AddressCommune;
import com.noc.employee_cv.provinces.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressCommuneRepo extends JpaRepository<AddressCommune,Integer> {
}

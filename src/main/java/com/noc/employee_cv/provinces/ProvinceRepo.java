package com.noc.employee_cv.provinces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepo extends JpaRepository<ProvinceCity,Integer> {



    }

package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.SpouseChildren;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepo extends JpaRepository<SpouseChildren,Integer>{
}

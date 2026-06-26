package com.noc.employee_cv.repository;

import com.noc.employee_cv.model.SpouseChildren;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepo extends JpaRepository<SpouseChildren,Integer>{
}

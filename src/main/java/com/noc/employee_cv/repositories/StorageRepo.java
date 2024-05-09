package com.noc.employee_cv.repositories;

import com.noc.employee_cv.models.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StorageRepo extends JpaRepository<ImageData,Long> {
    Optional<ImageData> findByName(String fileName);
}

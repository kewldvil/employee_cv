package com.noc.employee_cv.repositories;


import com.noc.employee_cv.models.FileUpload;
import com.noc.employee_cv.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUploadRepo extends JpaRepository<FileUpload,Integer> {
    List<FileUpload> findByUserId(Integer userId);

    FileUpload findByUserAndFileName(User user, String fileName);
    void deleteByUserAndFileName(User user, String fileName);
}

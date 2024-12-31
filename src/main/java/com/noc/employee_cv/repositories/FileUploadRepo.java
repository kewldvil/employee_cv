package com.noc.employee_cv.repositories;


import com.noc.employee_cv.dto.UserFileDTO;
import com.noc.employee_cv.models.FileUpload;
import com.noc.employee_cv.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FileUploadRepo extends JpaRepository<FileUpload,Integer> {
    List<FileUpload> findByUserId(Integer userId);
    FileUpload findByUserAndFileName(User user, String fileName);
    void deleteByUserAndFileName(User user, String fileName);
    @Query("SELECT new com.noc.employee_cv.dto.UserFileDTO(f.id, f.fileName) FROM FileUpload f WHERE f.user.id = :userId")
    List<UserFileDTO> findFileNamesByUserId( Integer userId);

}

package com.noc.employee_cv.services;

import com.noc.employee_cv.dto.UserFileDTO;
import com.noc.employee_cv.models.FileUpload;
import com.noc.employee_cv.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FileService {
    FileUpload uploadFile(MultipartFile fileUpload, User user);
    void deleteFile(Integer id);
    Optional<FileUpload> editFile(FileUpload fileUpload);
    List<FileUpload> getAllFiles();
    Optional<FileUpload> getFileById(Integer id);
    List<FileUpload> getFilesByUserId(Integer userId);
    List<UserFileDTO> getFileNamesByUserId(Integer userId);
    void deleteFileByUserIdAndFileName(Integer userId, String fileName);


}

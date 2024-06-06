package com.noc.employee_cv.services;

import com.noc.employee_cv.models.FileUpload;

import java.util.List;
import java.util.Optional;

public interface FileUploadService {
    FileUpload uploadFile(FileUpload fileUpload);
    void deleteFile(Integer id);
    Optional<FileUpload> editFile(FileUpload fileUpload);
    List<FileUpload> getAllFiles();
    Optional<FileUpload> getFileById(Integer id);
    List<FileUpload> getFilesByUserId(Integer userId);

}

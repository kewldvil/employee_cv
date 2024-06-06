package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.models.FileUpload;
import com.noc.employee_cv.repositories.FileUploadRepo;
import com.noc.employee_cv.services.FileUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class FileUploadServiceImp implements FileUploadService {
    private final FileUploadRepo fileUploadRepo;
    @Transactional
    @Override
    public FileUpload uploadFile(FileUpload fileUpload) {
        return fileUploadRepo.save(fileUpload);
    }

    @Override
    public void deleteFile(Integer id) {
        fileUploadRepo.deleteById(id);
    }

    @Override
    public Optional<FileUpload> editFile(FileUpload fileUpload) {
        if (fileUploadRepo.existsById(fileUpload.getId())) {
            return Optional.of(fileUploadRepo.save(fileUpload));
        }
        return Optional.empty();
    }

    @Override
    public List<FileUpload> getAllFiles() {
        return fileUploadRepo.findAll();
    }

    @Override
    public Optional<FileUpload> getFileById(Integer id) {
        return fileUploadRepo.findById(id);
    }
    @Transactional
    @Override
    public List<FileUpload> getFilesByUserId(Integer userId) {
        return fileUploadRepo.findByUserId(userId);
    }
}

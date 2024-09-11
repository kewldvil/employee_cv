package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.Bureau;
import com.noc.employee_cv.repositories.BureauRepo;
import com.noc.employee_cv.services.BureauService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BureauServiceImp implements BureauService {
    private final BureauRepo bureauRepo;
    @Override
    public void save(Bureau bureau) {
        bureauRepo.save(bureau);
    }

    @Override
    public Bureau findById(Integer id) {
        return null;
    }

    @Override
    public List<Bureau> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void update(Bureau bureau) {

    }
}

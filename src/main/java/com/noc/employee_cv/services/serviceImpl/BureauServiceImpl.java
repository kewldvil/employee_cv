package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.Bureau;
import com.noc.employee_cv.repository.BureauRepo;
import com.noc.employee_cv.services.BureauService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BureauServiceImpl implements BureauService {
    private final BureauRepo bureauRepo;

    @Override
    @Transactional
    public void save(Bureau bureau) {
        bureauRepo.save(bureau);
    }

    @Override
    public Bureau findById(Integer id) {
        return bureauRepo.findById(id).orElseThrow();
    }

    @Override
    public List<Bureau> findAll() {
        return bureauRepo.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        bureauRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Bureau bureau) {
        bureauRepo.save(bureau);
    }
}

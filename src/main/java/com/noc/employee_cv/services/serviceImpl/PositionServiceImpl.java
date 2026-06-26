package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.Position;
import com.noc.employee_cv.repository.PositionRepo;
import com.noc.employee_cv.services.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PositionServiceImpl implements PositionService {
    private final PositionRepo positionRepo;

    @Override
    @Transactional
    public void save(Position position) {
        positionRepo.save(position);
    }

    @Override
    public Position findById(Integer id) {
        return positionRepo.findById(id).orElseThrow();
    }

    @Override
    public Position findByPosition(String position) {
        return positionRepo.findByPosition(position);
    }

    @Override
    public List<Position> findAll() {
        return positionRepo.findAllByEnabledTrue();
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        positionRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Position position) {
        positionRepo.save(position);
    }
}

package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.models.Position;
import com.noc.employee_cv.repositories.PositionRepo;
import com.noc.employee_cv.services.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepo positionRepo;
    @Override
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
    public void deleteById(Integer id) {

    }

    @Override
    public void update(Position position) {
        positionRepo.save(position);
    }
}

package com.noc.employee_cv.services.serviceImp;

import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.repositories.CommuneRepo;
import com.noc.employee_cv.services.CommuneService;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommuneServiceImp implements CommuneService {
    private final CommuneRepo communeRepo;

    @Override
    public Commune getCommuneById(int id) {
        Optional<Commune> commune = communeRepo.findById(id);
        return commune.orElse(null);
    }

    @Override
    public List<Commune> getAllCommuneById(Integer districtId) {
        return communeRepo.findByDistrict_id(districtId);
    }
}

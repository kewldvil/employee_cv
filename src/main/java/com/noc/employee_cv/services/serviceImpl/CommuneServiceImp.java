package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.provinces.Commune;
import com.noc.employee_cv.repositories.CommuneRepo;
import com.noc.employee_cv.services.CommuneService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommuneServiceImp implements CommuneService {
    private final CommuneRepo communeRepo;

    @Override
    public Commune getCommuneById(Integer id) {
        Optional<Commune> commune = communeRepo.findById(id);
        return commune.orElse(null);
    }

    @Override
    public List<Commune> getAllCommuneById(Integer districtId) {
        return communeRepo.findByDistrictIdEAndEnabledTrue(districtId);
    }

    @Override
    public Commune save(Commune commune) {
        communeRepo.save(commune);
        return commune;
    }

    @Override
    public void update(Commune commune) {
        communeRepo.save(commune);
    }

    @Transactional
    public List<Commune> disableAndRetrieveCommunesByDistrictId(int districtId) {
        // Step 1: Perform the update
        communeRepo.updateSetEnabledFalseWhereDistrictId(districtId);

        // Step 2: Retrieve the updated communes
        return communeRepo.findDisabledCommunesByDistrictId(districtId);
    }


    public String getNextCommuneCode() {
        return communeRepo.findFirstByOrderByCommuneCodeDesc()
                .map(commune -> String.valueOf(Long.parseLong(commune.getCommune_code() + 1)))
                .orElse("1"); // Default to "1" if no record exists
    }
}

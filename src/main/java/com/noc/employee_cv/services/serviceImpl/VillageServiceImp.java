package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.provinces.Village;
import com.noc.employee_cv.repositories.VillageRepo;
import com.noc.employee_cv.services.VillageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VillageServiceImp implements VillageService {
    private final VillageRepo villageRepo;

    @Override
    public Village getVillageById(Integer id) {
        Optional<Village> village = villageRepo.findById(id);
        return village.orElse(null);
    }

    @Override
    public List<Village> getAllVillageById(Integer communeId) {
        return villageRepo.findByCommuneIdAndEnabledTrue(communeId);
    }

    @Override
    public void save(Village village) {
        villageRepo.save(village);
    }

    @Override
    public void update(Village village) {
        villageRepo.save(village);
    }
    public String getNextVillageCode() {
        return villageRepo.findFirstByOrderByVillageCodeDesc()
                .map(village -> String.valueOf(Integer.parseInt(village.getVillage_code()) + 1))
                .orElse("1"); // Default to "1" if no record exists
    }
}

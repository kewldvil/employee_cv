package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.Village;
import com.noc.employee_cv.repository.VillageRepo;
import com.noc.employee_cv.services.VillageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VillageServiceImpl implements VillageService {
    private final VillageRepo villageRepo;

    @Override
    public Village getVillageById(Integer id) {
        return villageRepo.findById(id).orElse(null);
    }

    @Override
    public List<Village> getAllVillageById(Integer communeId) {
        return villageRepo.findByCommuneIdAndEnabledTrue(communeId);
    }

    @Override
    @Transactional
    public void save(Village village) {
        villageRepo.save(village);
    }

    @Override
    @Transactional
    public void update(Village village) {
        villageRepo.save(village);
    }

    @Override
    @Transactional
    public int disableVillageByCommuneId(int communeId) {
        return villageRepo.updateSetEnabledFalseWhereCommuneId(communeId);
    }

    public String getNextVillageCode() {
        return villageRepo.findFirstByOrderByVillageCodeDesc()
                .map(village -> String.valueOf(Integer.parseInt(village.getVillage_code()) + 1))
                .orElse("1");
    }
}

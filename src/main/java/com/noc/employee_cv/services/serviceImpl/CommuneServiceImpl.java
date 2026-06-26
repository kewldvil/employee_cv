package com.noc.employee_cv.services.serviceImpl;

import com.noc.employee_cv.model.Commune;
import com.noc.employee_cv.repository.CommuneRepo;
import com.noc.employee_cv.services.CommuneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommuneServiceImpl implements CommuneService {
    private final CommuneRepo communeRepo;

    @Override
    public Commune getCommuneById(Integer id) {
        return communeRepo.findById(id).orElse(null);
    }

    @Override
    public List<Commune> getAllCommuneById(Integer districtId) {
        return communeRepo.findByDistrictIdEAndEnabledTrue(districtId);
    }

    @Override
    @Transactional
    public Commune save(Commune commune) {
        return communeRepo.save(commune);
    }

    @Override
    @Transactional
    public void update(Commune commune) {
        communeRepo.save(commune);
    }

    @Override
    @Transactional
    public List<Commune> disableAndRetrieveCommunesByDistrictId(int districtId) {
        communeRepo.updateSetEnabledFalseWhereDistrictId(districtId);
        return communeRepo.findDisabledCommunesByDistrictId(districtId);
    }

    public String getNextCommuneCode() {
        return communeRepo.findFirstByOrderByCommuneCodeDesc()
                .map(commune -> String.valueOf(Long.parseLong(commune.getCommune_code()) + 1))
                .orElse("1");
    }
}

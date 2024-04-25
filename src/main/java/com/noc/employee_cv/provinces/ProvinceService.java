package com.noc.employee_cv.provinces;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceService {
    @Autowired
    private final ProvinceRepo provinceRepo;


    public List<ProvinceCity> getProvinces(EntityManager entityManager) {
        @SuppressWarnings("unchecked")
        List<ProvinceCity> provinceCities=(List<ProvinceCity>) entityManager.createNativeQuery("select * from province_city",ProvinceCity.class).getResultList();
        return provinceCities;
    }

    public List<District> getDistrict(EntityManager entityManager, Integer provinceId) {
        @SuppressWarnings("unchecked")
        List<District> districts = (List<District>) entityManager.createNativeQuery("select * from district where province_city_id = " + provinceId,District.class).getResultList();;
        return districts;
    }

    public List<Commune> getCommune(EntityManager entityManager, Integer districtId) {
        @SuppressWarnings("unchecked")
        List<Commune> communes = (List<Commune>) entityManager.createNativeQuery("select * from commune where district_id = " + districtId,Commune.class).getResultList();;
        return communes;
    }

    public List<Village> getVillage(EntityManager entityManager, Integer communeId) {
        @SuppressWarnings("unchecked")
        List<Village> communes = (List<Village>) entityManager.createNativeQuery("select * from village where commune_id = " + communeId,Village.class).getResultList();;
        return communes;
    }
}

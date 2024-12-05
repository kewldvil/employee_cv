package com.noc.employee_cv.provinces;

import com.noc.employee_cv.records.CommuneRecord;
import com.noc.employee_cv.records.DistrictRecord;
import com.noc.employee_cv.services.serviceImpl.CommuneServiceImp;
import com.noc.employee_cv.services.serviceImpl.DistrictServiceImp;
import com.noc.employee_cv.services.serviceImpl.ProvinceCityServiceImp;
import com.noc.employee_cv.services.serviceImpl.VillageServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final ProvinceCityServiceImp provinceService;
    private final DistrictServiceImp districtService;
    private final CommuneServiceImp communeService;
    private final VillageServiceImp villageService;

    //    PROVINCES
    @GetMapping("/provinces")
    public ResponseEntity<List<ProvinceCity>> province() {
        List<ProvinceCity> provinceCities = provinceService.getAllProvinceCities();
        if (provinceCities.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(provinceCities); // Return 200 OK with body

    }

    //    END PROVINCES
// DISTRICTS
    @GetMapping("/districts")
    public ResponseEntity<List<District>> district(@RequestParam(required = false, defaultValue = "0") Integer province_id) {
        if (province_id == 0) {
            return ResponseEntity.ok(Collections.emptyList()); // Return empty list if province_id is default value (0)
        }

        List<District> districts = districtService.getAllDistrictById(province_id);

        if (districts.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }

        return ResponseEntity.ok(districts); // Return 200 OK with body
    }

    @PostMapping("/districts")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<District> createDistrict(@RequestBody DistrictRecord district) {
        District ds = new District();
        ds.setProvince_city_id(district.province_city_id());
        ds.setDistrict_code(districtService.getNextDistrictCode());
        ds.setDistrict_name_en(district.district_name_en());
        ds.setDistrict_name_kh(district.district_name_kh());
        ds.setEnabled(true);
        districtService.save(ds);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/districts")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<District> updateDistrict(@RequestBody DistrictRecord district) {
        District ds = new District();
        ds.setId(district.id());
        ds.setEnabled(district.enabled());
        ds.setDistrict_code(district.district_code());
        ds.setProvince_city_id(district.province_city_id());
        ds.setDistrict_name_en(district.district_name_en());
        ds.setDistrict_name_kh(district.district_name_kh());
        districtService.update(ds);
        return ResponseEntity.accepted().build();
    }

//    END DISTRICTS
//  START COMMUNES
    @GetMapping("/communes")
    public ResponseEntity<List<Commune>> commune(@RequestParam(required = false, defaultValue = "0") Integer district_id) {
        if (district_id == 0) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<Commune> communes = communeService.getAllCommuneById(district_id);
        if (communes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }
        return ResponseEntity.ok(communes); // Return 200 OK with body

    }

    @PostMapping("/communes")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<District> createCommune(@RequestBody CommuneRecord commune) {
        Commune cm = new Commune();
        cm.setDistrict_id(commune.district_id());
        cm.setCommune_code(communeService.getNextCommuneCode());
        cm.setCommune_name_en(commune.commune_name_en());
        cm.setCommune_name_kh(commune.commune_name_kh());
        cm.setEnabled(true);
        communeService.save(cm);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/communes")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Commune> updateCommune(@RequestBody CommuneRecord commune) {
        Commune cm = new Commune();
        cm.setId(commune.id());
        cm.setEnabled(commune.enabled());
        cm.setCommune_code(commune.commune_code());
        cm.setDistrict_id(commune.district_id());
        cm.setCommune_name_en(commune.commune_name_en());
        cm.setCommune_name_kh(commune.commune_name_kh());
        communeService.update(cm);
        return ResponseEntity.accepted().build();
    }

//    END COMMUNES

    @GetMapping("/villages")
    public ResponseEntity<List<Village>> village(@RequestParam(required = false, defaultValue = "0") Integer commune_id) {
        if (commune_id == 0) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<Village> villages = villageService.getAllVillageById(commune_id);
        if (villages.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }
        return ResponseEntity.ok(villages); // Return 200 OK with body

    }

}



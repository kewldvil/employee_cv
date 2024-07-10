package com.noc.employee_cv.provinces;

import com.noc.employee_cv.services.serviceImpl.CommuneServiceImp;
import com.noc.employee_cv.services.serviceImpl.DistrictServiceImp;
import com.noc.employee_cv.services.serviceImpl.ProvinceCityServiceImp;
import com.noc.employee_cv.services.serviceImpl.VillageServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class ProvinceController {
    private final ProvinceCityServiceImp provinceService;
    private final DistrictServiceImp districtService;
    private final CommuneServiceImp communeService;
    private final VillageServiceImp villageService;


    @GetMapping("/provinces")
    public ResponseEntity<List<ProvinceCity>> province() {
        List<ProvinceCity> provinceCities = provinceService.getAllProvinceCities();
        if (provinceCities.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(provinceCities); // Return 200 OK with body

    }
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



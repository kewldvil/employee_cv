package com.noc.employee_cv.provinces;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class ProvinceController {
    @Autowired
    private final ProvinceService service;
    private final EntityManager entityManager;

    @GetMapping("/provinces")
    public ResponseEntity<List<ProvinceCity>> province() {
        List<ProvinceCity> provinceCities = service.getProvinces(entityManager);
        if (provinceCities.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }
        return ResponseEntity.ok(provinceCities); // Return 200 OK with body

    }
    @GetMapping("/districts")
    public ResponseEntity<List<District>> district(@RequestParam Integer province_id) {
        List<District> districts = service.getDistrict(entityManager,province_id);
        if (districts.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }
        return ResponseEntity.ok(districts); // Return 200 OK with body

    }
    @GetMapping("/communes")
    public ResponseEntity<List<Commune>> commune(@RequestParam Integer district_id) {
        List<Commune> communes = service.getCommune(entityManager,district_id);
        if (communes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }
        return ResponseEntity.ok(communes); // Return 200 OK with body

    }
    @GetMapping("/villages")
    public ResponseEntity<List<Village>> village(@RequestParam Integer commune_id) {
        List<Village> villages = service.getVillage(entityManager,commune_id);
        if (villages.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content
        }
        return ResponseEntity.ok(villages); // Return 200 OK with body

    }

}



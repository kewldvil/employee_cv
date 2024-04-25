package com.noc.employee_cv.contollers;

import com.noc.employee_cv.models.UniversitySkill;
import com.noc.employee_cv.services.UniversitySkillService;
import com.noc.employee_cv.services.UniversitySkillServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/university-skills")
@RequiredArgsConstructor
public class UniversitySkillController {
    private final UniversitySkillServiceImp service;
    @GetMapping("/")
    public ResponseEntity<List<UniversitySkill>> getUniversitySkills() {
        return ResponseEntity.ok(service.findAll());
    }
}

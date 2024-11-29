package com.noc.employee_cv.contollers;

import com.noc.employee_cv.dto.DepartmentDTO;
import com.noc.employee_cv.dto.EmployeeSkillDTO;
import com.noc.employee_cv.dto.GeneralDepartmentDTO;
import com.noc.employee_cv.models.Department;
import com.noc.employee_cv.models.GeneralDepartment;
import com.noc.employee_cv.models.Skill;
import com.noc.employee_cv.services.serviceImpl.SkillServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skill")
@RequiredArgsConstructor
public class SkillController {
    private final SkillServiceImpl skillServiceImpl;


    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> createSkill(@RequestBody EmployeeSkillDTO skillDTO) {
        Skill skill = skillServiceImpl.findByName(skillDTO.getSkillName());
        if (skill != null) {
            // Return an error message with 400 Bad Request status
            return ResponseEntity.badRequest().body("Skill already exists");
        }

        // Create and save a new skill
        skill = new Skill();
        skill.setSkillName(skillDTO.getSkillName());
        skill.setEnabled(skillDTO.isEnabled());
        skillServiceImpl.save(skill);

        // Return the created skill with 202 Accepted status
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(skill);
    }
    @GetMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillServiceImpl.findAll());
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Skill> updateSkill(
            @RequestBody EmployeeSkillDTO skillDTO) {
            Skill skill = new Skill();
            skill.setId(skillDTO.getId());
            skill.setSkillName(skillDTO.getSkillName());
            skill.setEnabled(skillDTO.isEnabled());
            skillServiceImpl.update(skill);
        return ResponseEntity.accepted().build();
    }
}

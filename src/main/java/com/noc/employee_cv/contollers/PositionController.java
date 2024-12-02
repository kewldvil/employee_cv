package com.noc.employee_cv.contollers;

import com.noc.employee_cv.dto.EmployeeSkillDTO;
import com.noc.employee_cv.models.Position;
import com.noc.employee_cv.models.Skill;
import com.noc.employee_cv.records.PositionRecord;
import com.noc.employee_cv.services.serviceImpl.PositionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/positions")
public class PositionController {
    private final PositionServiceImpl positionService;

    @PostMapping("/")
    public ResponseEntity<?> createPosition(@RequestBody PositionRecord positionRecord) {
        Position position = positionService.findByPosition(positionRecord.position());
        if (position != null) {
            return ResponseEntity.badRequest().body("Position already exists");
        }
        position = new Position();
        position.setPosition(positionRecord.position());
        position.setEnabled(positionRecord.isEnabled());
        positionService.save(position);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(position);
    }

    @GetMapping("/")
    public ResponseEntity<List<Position>> getPositions() {
        return ResponseEntity.ok(positionService.findAll());
    }

    @PutMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Position> updatePosition(
            @RequestBody PositionRecord positionRecord) {
        System.out.println(positionRecord);
        Position position = new Position();
        position.setId(positionRecord.id());
        position.setPosition(positionRecord.position());
        position.setEnabled(positionRecord.isEnabled());
        positionService.update(position);
        return ResponseEntity.accepted().build();
    }
}

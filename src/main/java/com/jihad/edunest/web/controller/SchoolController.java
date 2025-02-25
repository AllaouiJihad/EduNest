package com.jihad.edunest.web.controller;

import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.enums.SchoolStatus;
import com.jihad.edunest.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;
    @PostMapping
    public ResponseEntity<School> createSchool(@RequestBody School school) {
        School createdSchool = schoolService.createSchool(school);
        return ResponseEntity.ok(createdSchool);
    }

    @GetMapping("/{id}")
    public ResponseEntity<School> getSchoolById(@PathVariable Long id) {
        return schoolService.getSchoolById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<School>> getAllSchools() {
        List<School> schools = schoolService.getAllSchools();
        return ResponseEntity.ok(schools);
    }

    @PutMapping("/{id}")
    public ResponseEntity<School> updateSchool(@PathVariable Long id, @RequestBody School updatedSchool) {
        School school = schoolService.updateSchool(id, updatedSchool);
        return ResponseEntity.ok(school);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<School>> getSchoolsByStatus(@PathVariable SchoolStatus status) {
        List<School> schools = schoolService.getSchoolsByStatus(status);
        return ResponseEntity.ok(schools);
    }
}

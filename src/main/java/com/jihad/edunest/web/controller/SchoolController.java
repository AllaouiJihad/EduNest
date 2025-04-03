package com.jihad.edunest.web.controller;

import com.jihad.edunest.service.SchoolService;
import com.jihad.edunest.web.vms.request.school.SchoolSearchRequest;
import com.jihad.edunest.web.vms.responce.school.SchoolDetailsResponse;
import com.jihad.edunest.web.vms.responce.school.SchoolSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/search")
    public ResponseEntity<Page<SchoolSearchResponse>> searchSchools(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String postalCode,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Float maxRating,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {

        SchoolSearchRequest request = SchoolSearchRequest.builder()
                .name(name)
                .city(city)
                .postalCode(postalCode)
                .categoryId(categoryId)
                .minRating(minRating)
                .maxRating(maxRating)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        return ResponseEntity.ok(schoolService.searchSchools(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolDetailsResponse> getSchoolDetails(@PathVariable Long id) {
        return ResponseEntity.ok(schoolService.getSchoolDetails(id));
    }

    @GetMapping
    public ResponseEntity<Page<SchoolSearchResponse>> getAllSchools(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        return ResponseEntity.ok(schoolService.getAllSchools(page, size, sortBy, sortDirection));
    }

    @GetMapping("/active")
    public ResponseEntity<List<SchoolSearchResponse>> getAllActiveSchools() {
        return ResponseEntity.ok(schoolService.getAllActiveSchools());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<SchoolSearchResponse>> getSchoolsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(schoolService.getSchoolsByCategory(categoryId, page, size));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<Page<SchoolSearchResponse>> getSchoolsByCity(
            @PathVariable String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(schoolService.getSchoolsByCity(city, page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        boolean deleted = schoolService.deleteSchool(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

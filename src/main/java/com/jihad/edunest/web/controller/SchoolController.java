package com.jihad.edunest.web.controller;

import com.jihad.edunest.service.SchoolService;
import com.jihad.edunest.web.vms.request.school.SchoolSearchRequest;
import com.jihad.edunest.web.vms.responce.school.SchoolDetailsResponse;
import com.jihad.edunest.web.vms.responce.school.SchoolSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/search")
    public ResponseEntity<Page<SchoolSearchResponse>> searchSchools(SchoolSearchRequest request) {
        return ResponseEntity.ok(schoolService.searchSchools(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolDetailsResponse> getSchoolDetails(@PathVariable Long id) {
        return ResponseEntity.ok(schoolService.getSchoolDetails(id));
    }
}

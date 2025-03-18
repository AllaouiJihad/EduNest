package com.jihad.edunest.web.controller;

import com.jihad.edunest.service.SchoolComparisonService;
import com.jihad.edunest.web.vms.request.school.SchoolComparisonRequest;
import com.jihad.edunest.web.vms.responce.school.SchoolComparisonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools/comparison")
@RequiredArgsConstructor
public class SchoolComparisonController {

    private final SchoolComparisonService comparisonService;

    @PostMapping
    public ResponseEntity<SchoolComparisonResponse> compareSchools(@RequestBody SchoolComparisonRequest request) {
        return ResponseEntity.ok(comparisonService.compareSchools(request));
    }
}

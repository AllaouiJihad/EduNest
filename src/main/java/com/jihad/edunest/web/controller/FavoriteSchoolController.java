package com.jihad.edunest.web.controller;

import com.jihad.edunest.service.FavoriteSchoolService;
import com.jihad.edunest.web.vms.responce.school.FavoriteSchoolDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteSchoolController {
    private final FavoriteSchoolService favoriteSchoolService;

    @GetMapping
    public ResponseEntity<List<FavoriteSchoolDTO>> getFavoriteSchools(Authentication authentication) {
        return ResponseEntity.ok(favoriteSchoolService.getFavoriteSchools(authentication.getName()));
    }

    @PostMapping("/{schoolId}")
    public ResponseEntity<Void> addFavoriteSchool(
            Authentication authentication,
            @PathVariable Long schoolId,
            @RequestParam(required = false) String notes) {

        favoriteSchoolService.addFavoriteSchool(authentication.getName(), schoolId, notes);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{schoolId}")
    public ResponseEntity<Void> removeFavoriteSchool(
            Authentication authentication,
            @PathVariable Long schoolId) {

        favoriteSchoolService.removeFavoriteSchool(authentication.getName(), schoolId);
        return ResponseEntity.ok().build();
    }
}

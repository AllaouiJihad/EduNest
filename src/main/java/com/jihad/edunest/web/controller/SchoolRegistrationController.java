package com.jihad.edunest.web.controller;

import com.jihad.edunest.service.SchoolRegistrationService;
import com.jihad.edunest.web.vms.request.school.SchoolRegistrationRequestDto;
import com.jihad.edunest.web.vms.request.school.SchoolRegistrationReviewDto;
import com.jihad.edunest.web.vms.responce.school.SchoolRegistrationResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools/registration")
@RequiredArgsConstructor
public class SchoolRegistrationController {

    private final SchoolRegistrationService registrationService;

    /**
     * Endpoint pour soumettre une demande d'enregistrement d'école
     */
    @PostMapping("/submit")
    public ResponseEntity<SchoolRegistrationResponseDto> submitSchoolRegistration(
            @RequestBody @Valid SchoolRegistrationRequestDto requestDto) {
        return ResponseEntity.ok(registrationService.submitSchoolRegistrationRequest(requestDto));
    }

    /**
     * Endpoint pour récupérer les demandes de l'utilisateur connecté
     */
    @GetMapping("/my-requests")
    public ResponseEntity<List<SchoolRegistrationResponseDto>> getMyRequests() {
        return ResponseEntity.ok(registrationService.getMemberRequests());
    }

    /**
     * Endpoint pour récupérer toutes les demandes en attente (admin)
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SchoolRegistrationResponseDto>> getPendingRequests() {
        return ResponseEntity.ok(registrationService.getPendingRequests());
    }

    /**
     * Endpoint pour traiter une demande (approuver/rejeter)
     */
    @PostMapping("/review")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SchoolRegistrationResponseDto> reviewRequest(
            @RequestBody @Valid SchoolRegistrationReviewDto reviewDto) {
        return ResponseEntity.ok(registrationService.reviewRegistrationRequest(reviewDto));
    }
}

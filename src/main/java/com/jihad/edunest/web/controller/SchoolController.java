package com.jihad.edunest.web.controller;

import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.enums.SchoolStatus;
import com.jihad.edunest.exception.user.UserNotFoundException;
import com.jihad.edunest.service.CategoryService;
import com.jihad.edunest.service.SchoolService;
import com.jihad.edunest.web.vms.mapper.SchoolMapper;
import com.jihad.edunest.web.vms.request.SchoolCreateDTO;
import com.jihad.edunest.web.vms.responce.SchoolResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;
    private final MemberService memberService;
    private final CategoryService categoryService;

    /**
     * Crée une nouvelle école
     */
    @PostMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<SchoolResponseDTO> createSchool(
            @RequestBody @Valid SchoolCreateDTO schoolDTO,
            Authentication authentication) {

        // Récupérer l'utilisateur authentifié
        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Créer l'école
        School school = schoolService.createSchool(schoolDTO, member.getId());

        // Retourner la réponse
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SchoolMapper.toResponseDTO(school));
    }

    /**
     * Récupère une école par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponseDTO> getSchool(@PathVariable Long id) {
        School school = schoolService.getSchoolById(id);
        return ResponseEntity.ok(SchoolMapper.toResponseDTO(school));
    }

    /**
     * Récupère toutes les écoles approuvées
     */
    @GetMapping
    public ResponseEntity<List<SchoolResponseDTO>> getAllApprovedSchools() {
        List<School> schools = schoolService.getAllApprovedSchools();
        List<SchoolResponseDTO> schoolDTOs = schools.stream()
                .map(SchoolMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(schoolDTOs);
    }

    /**
     * Récupère les écoles administrées par l'utilisateur connecté
     */
    @GetMapping("/my-schools")
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    public ResponseEntity<List<SchoolResponseDTO>> getMySchools(Authentication authentication) {
        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        List<School> schools = schoolService.getSchoolsByAdministratorId(member.getId());
        List<SchoolResponseDTO> schoolDTOs = schools.stream()
                .map(SchoolMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(schoolDTOs);
    }

    /**
     * Met à jour le statut d'une école (pour les admins)
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SchoolResponseDTO> updateSchoolStatus(
            @PathVariable Long id,
            @RequestParam SchoolStatus status) {

        School updatedSchool = schoolService.updateSchoolStatus(id, status);
        return ResponseEntity.ok(SchoolMapper.toResponseDTO(updatedSchool));
    }

    /**
     * Met à jour les informations d'une école
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEMBER', 'ADMIN')")
    public ResponseEntity<SchoolResponseDTO> updateSchool(
            @PathVariable Long id,
            @RequestBody @Valid SchoolUpdateDTO schoolDTO,
            Authentication authentication) {

        // Vérifier les autorisations
        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Seul l'admin ou l'administrateur de l'école peut modifier l'école
        if (!member.getRole().equals(UserRole.ADMIN)) {
            schoolService.verifySchoolAdministrator(id, member.getId());
        }

        School updatedSchool = schoolService.updateSchool(id, schoolDTO);
        return ResponseEntity.ok(SchoolMapper.toResponseDTO(updatedSchool));
    }

    /**
     * Recherche d'écoles par critères
     */
    @GetMapping("/search")
    public ResponseEntity<List<SchoolResponseDTO>> searchSchools(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Long categoryId) {

        SchoolSearchDTO searchDTO = new SchoolSearchDTO();
        searchDTO.setName(name);
        searchDTO.setCity(city);
        searchDTO.setCategoryId(categoryId);

        List<School> schools = schoolService.searchSchools(searchDTO);
        List<SchoolResponseDTO> schoolDTOs = schools.stream()
                .map(SchoolMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(schoolDTOs);
    }

    /**
     * Ajoute une école aux favoris de l'utilisateur
     */
    @PostMapping("/{id}/favorite")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Void> addToFavorites(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        schoolService.addToFavorites(id, member.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Retire une école des favoris de l'utilisateur
     */
    @DeleteMapping("/{id}/favorite")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Void> removeFromFavorites(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        schoolService.removeFromFavorites(id, member.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * Ajoute une critique pour une école
     */
    @PostMapping("/{id}/reviews")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable Long id,
            @RequestBody @Valid ReviewCreateDTO reviewDTO,
            Authentication authentication) {

        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        Review review = schoolService.addReview(id, member.getId(), reviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReviewMapper.toDTO(review));
    }

    /**
     * Récupère les critiques d'une école
     */
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> getSchoolReviews(@PathVariable Long id) {
        List<Review> reviews = schoolService.getSchoolReviews(id);
        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviewDTOs);
    }

    /**
     * Envoie une demande de contact à une école
     */
    @PostMapping("/{id}/contact")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ContactRequestDTO> contactSchool(
            @PathVariable Long id,
            @RequestBody @Valid ContactRequestCreateDTO contactDTO,
            Authentication authentication) {

        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        ContactRequest contactRequest = schoolService.createContactRequest(id, member.getId(), contactDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ContactRequestMapper.toDTO(contactRequest));
    }
}

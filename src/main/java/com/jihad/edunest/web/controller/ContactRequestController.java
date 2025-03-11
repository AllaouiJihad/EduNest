package com.jihad.edunest.web.controller;

import com.jihad.edunest.domaine.entities.ContactRequest;
import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.enums.ContactStatus;
import com.jihad.edunest.domaine.enums.UserRole;
import com.jihad.edunest.exception.user.UserNotFoundException;
import com.jihad.edunest.service.ContactRequestService;
import com.jihad.edunest.web.vms.mapper.ContactRequestMapper;
import com.jihad.edunest.web.vms.request.ContactRequestCreateDTO;
import com.jihad.edunest.web.vms.responce.ContactRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-requests")
@RequiredArgsConstructor
public class ContactRequestController {
    private final ContactRequestService contactRequestService;
    private final MemberService memberService;

    /**
     * Crée une demande de contact
     */
    @PostMapping("/schools/{schoolId}")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<ContactRequestDTO> createContactRequest(
            @PathVariable Long schoolId,
            @RequestBody @Valid ContactRequestCreateDTO requestDTO,
            Authentication authentication) {

        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        ContactRequest contactRequest = contactRequestService.createContactRequest(
                requestDTO, member.getId(), schoolId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ContactRequestMapper.toDTO(contactRequest));
    }

    /**
     * Récupère les demandes de contact d'un membre
     */
    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<List<ContactRequestDTO>> getMyContactRequests(Authentication authentication) {
        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        List<ContactRequest> contactRequests = contactRequestService.getContactRequestsByMember(member.getId());
        return ResponseEntity.ok(ContactRequestMapper.toDTOList(contactRequests));
    }

    /**
     * Récupère les demandes de contact pour une école (admin de l'école seulement)
     */
    @GetMapping("/schools/{schoolId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ResponseEntity<List<ContactRequestDTO>> getSchoolContactRequests(
            @PathVariable Long schoolId,
            Authentication authentication) {

        // Vérifier que l'utilisateur est admin ou administrateur de l'école
        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        if (!member.getRole().equals(UserRole.ADMIN)) {
            // Vérifier si l'utilisateur est bien administrateur de cette école
            boolean isSchoolAdmin = false;

            // À remplacer par la vérification appropriée selon votre modèle
            if (member.getRole().equals(UserRole.MEMBER)) {
                isSchoolAdmin = schoolService.isSchoolAdministrator(schoolId, member.getId());
            }

            if (!isSchoolAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        List<ContactRequest> contactRequests = contactRequestService.getContactRequestsBySchool(schoolId);
        return ResponseEntity.ok(ContactRequestMapper.toDTOList(contactRequests));
    }

    /**
     * Met à jour le statut d'une demande de contact
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ResponseEntity<ContactRequestDTO> updateContactRequestStatus(
            @PathVariable Long id,
            @RequestParam ContactStatus status,
            Authentication authentication) {

        // Vérifier que l'utilisateur a le droit de modifier cette demande
        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        ContactRequest contactRequest = contactRequestService.getContactRequestById(id);

        if (!member.getRole().equals(UserRole.ADMIN)) {
            // Si ce n'est pas un admin, vérifier que c'est bien l'admin de l'école concernée
            boolean isSchoolAdmin = false;

            // À remplacer par la vérification appropriée selon votre modèle
            if (member.getRole().equals(UserRole.MEMBER) && contactRequest.getSchool() != null) {
                isSchoolAdmin = schoolService.isSchoolAdministrator(contactRequest.getSchool().getId(), member.getId());
            }

            if (!isSchoolAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        ContactRequest updatedRequest = contactRequestService.updateStatus(id, status);
        return ResponseEntity.ok(ContactRequestMapper.toDTO(updatedRequest));
    }

    /**
     * Récupère une demande de contact par son ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEMBER')")
    public ResponseEntity<ContactRequestDTO> getContactRequest(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        Member member = memberService.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        ContactRequest contactRequest = contactRequestService.getContactRequestById(id);

        // Vérifier que l'utilisateur a le droit de voir cette demande
        if (!member.getRole().equals(UserRole.ADMIN)) {
            boolean canView = false;

            // Si c'est le membre qui a fait la demande
            if (contactRequest.getMember() != null &&
                    contactRequest.getMember().getId().equals(member.getId())) {
                canView = true;
            }

            // Ou si c'est l'admin de l'école concernée
            if (!canView && contactRequest.getSchool() != null) {
                canView = schoolService.isSchoolAdministrator(contactRequest.getSchool().getId(), member.getId());
            }

            if (!canView) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(ContactRequestMapper.toDTO(contactRequest));
    }

    /**
     * Supprime une demande de contact (admin seulement)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteContactRequest(@PathVariable Long id) {
        contactRequestService.deleteContactRequest(id);
        return ResponseEntity.noContent().build();
    }
}

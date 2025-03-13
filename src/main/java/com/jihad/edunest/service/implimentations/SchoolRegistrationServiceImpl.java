package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.*;
import com.jihad.edunest.domaine.enums.RequestStatus;
import com.jihad.edunest.domaine.enums.SchoolStatus;
import com.jihad.edunest.domaine.enums.UserRole;

import com.jihad.edunest.exception.ApiException;
import com.jihad.edunest.repository.*;
import com.jihad.edunest.service.SchoolRegistrationService;
import com.jihad.edunest.service.auth.EmailService;
import com.jihad.edunest.web.vms.request.school.SchoolRegistrationRequestDto;
import com.jihad.edunest.web.vms.request.school.SchoolRegistrationReviewDto;
import com.jihad.edunest.web.vms.responce.school.SchoolRegistrationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolRegistrationServiceImpl implements SchoolRegistrationService {

    private final SchoolRegistrationRequestRepository registrationRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final SchoolRepository schoolRepository;
    private final SchoolAdministratorRepository schoolAdminRepository;
    private final EmailService emailService;

    /**
     * Méthode pour soumettre une demande d'enregistrement d'école
     */
    @Override
    @Transactional
    public SchoolRegistrationResponseDto submitSchoolRegistrationRequest(SchoolRegistrationRequestDto requestDto) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member currentMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié"));

        // Récupérer la catégorie
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Catégorie non trouvée"));

        // Créer la demande d'enregistrement
        SchoolRegistrationRequest request = SchoolRegistrationRequest.builder()
                .member(currentMember)
                .schoolName(requestDto.getSchoolName())
                .schoolAddress(requestDto.getSchoolAddress())
                .schoolPostalCode(requestDto.getSchoolPostalCode())
                .schoolCity(requestDto.getSchoolCity())
                .schoolPhoneNumber(requestDto.getSchoolPhoneNumber())
                .schoolEmail(requestDto.getSchoolEmail())
                .schoolWebsite(requestDto.getSchoolWebsite())
                .schoolDescription(requestDto.getSchoolDescription())
                .category(category)
                .additionalInfo(requestDto.getAdditionalInfo())
                .status(RequestStatus.PENDING)
                .submittedAt(LocalDateTime.now())
                .build();

        SchoolRegistrationRequest savedRequest = registrationRepository.save(request);

        // Envoyer une notification par email à l'administrateur (optionnel)
        // emailService.sendSchoolRegistrationNotification(currentMember.getEmail(), savedRequest.getId());

        return mapToResponseDto(savedRequest);
    }

    /**
     * Méthode pour récupérer toutes les demandes d'un utilisateur
     */
    @Override
    public List<SchoolRegistrationResponseDto> getMemberRequests() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member currentMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié"));

        return registrationRepository.findByMemberId(currentMember.getId()).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Méthode pour récupérer toutes les demandes en attente (pour les administrateurs)
     */
    @Override
    public List<SchoolRegistrationResponseDto> getPendingRequests() {
        return registrationRepository.findByStatus(RequestStatus.PENDING).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Méthode pour examiner une demande d'enregistrement (approuver ou rejeter)
     */
    @Override
    @Transactional
    public SchoolRegistrationResponseDto reviewRegistrationRequest(SchoolRegistrationReviewDto reviewDto) {
        // Vérifier que l'utilisateur est un administrateur
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member admin = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Utilisateur non authentifié"));

        if (!admin.getRole().equals(UserRole.ADMIN)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Vous n'avez pas les droits pour effectuer cette action");
        }

        // Récupérer la demande
        SchoolRegistrationRequest request = registrationRepository.findById(reviewDto.getRequestId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Demande d'enregistrement non trouvée"));

        // Vérifier que la demande est en attente
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cette demande a déjà été traitée");
        }

        // Mettre à jour le statut de la demande
        request.setStatus(reviewDto.getStatus());
        request.setReviewedAt(LocalDateTime.now());
        request.setAdminReviewNotes(reviewDto.getAdminReviewNotes());

        // Si la demande est approuvée, créer l'école et l'administrateur
        if (reviewDto.getStatus().equals(RequestStatus.APPROVED)) {
            // Créer l'école
            School school = School.builder()
                    .name(request.getSchoolName())
                    .address(request.getSchoolAddress())
                    .postalCode(request.getSchoolPostalCode())
                    .city(request.getSchoolCity())
                    .phoneNumber(request.getSchoolPhoneNumber())
                    .email(request.getSchoolEmail())
                    .website(request.getSchoolWebsite())
                    .description(request.getSchoolDescription())
                    .category(request.getCategory())
                    .status(SchoolStatus.APPROVED)
                    .registrationRequest(request)
                    .build();

            School savedSchool = schoolRepository.save(school);

            // Créer l'administrateur de l'école
            SchoolAdministrator schoolAdmin = SchoolAdministrator.builder()
                    .member(request.getMember())
                    .school(savedSchool)
                    .assignedDate(LocalDateTime.now())
                    .isActive(true)
                    .build();

            schoolAdminRepository.save(schoolAdmin);

            // Mettre à jour le rôle du membre
            Member member = request.getMember();
            member.setRole(UserRole.SCHOOL_ADMIN);
            memberRepository.save(member);

            // Associer l'école à la demande
            request.setCreatedSchool(savedSchool);

            // Envoyer un email de confirmation
            emailService.sendSchoolApprovalEmail(member.getEmail(), savedSchool.getName());
        } else if (reviewDto.getStatus().equals(RequestStatus.REJECTED)) {
            // Envoyer un email de rejet
            emailService.sendSchoolRejectionEmail(request.getMember().getEmail(), request.getSchoolName(), reviewDto.getAdminReviewNotes());
        }

        return mapToResponseDto(registrationRepository.save(request));
    }

    /**
     * Méthode utilitaire pour mapper l'entité vers DTO
     */
    private SchoolRegistrationResponseDto mapToResponseDto(SchoolRegistrationRequest request) {
        return SchoolRegistrationResponseDto.builder()
                .id(request.getId())
                .schoolName(request.getSchoolName())
                .status(request.getStatus())
                .submittedAt(request.getSubmittedAt())
                .reviewedAt(request.getReviewedAt())
                .adminReviewNotes(request.getAdminReviewNotes())
                .build();
    }
}

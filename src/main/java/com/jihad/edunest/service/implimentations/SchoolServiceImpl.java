package com.jihad.edunest.service.implimentations;
import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.enums.SchoolStatus;
import com.jihad.edunest.repository.SchoolRepository;
import com.jihad.edunest.service.SchoolService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository schoolRepository;
    private final MemberRepository memberRepository;
    private final SchoolAdministratorRepository schoolAdminRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public School createSchool(SchoolCreateDTO schoolDTO, Long memberId) {
        // Vérifier si le membre existe
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + memberId));

        // Vérifier si l'utilisateur a déjà une école
        schoolAdminRepository.findByMemberId(memberId).ifPresent(admin -> {
            throw new IllegalStateException("User already administrates a school");
        });

        // Créer l'école
        School school = new School();
        school.setName(schoolDTO.getName());
        school.setAddress(schoolDTO.getAddress());
        school.setPostalCode(schoolDTO.getPostalCode());
        school.setCity(schoolDTO.getCity());
        school.setPhoneNumber(schoolDTO.getPhoneNumber());
        school.setEmail(schoolDTO.getEmail());
        school.setWebsite(schoolDTO.getWebsite());
        school.setDescription(schoolDTO.getDescription());
        school.setStatus(SchoolStatus.PENDING); // Status initial en attente de validation

        // Définir la catégorie
        if (schoolDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(schoolDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            school.setCategory(category);
        }

        // Sauvegarder l'école
        School savedSchool = schoolRepository.save(school);

        // Créer l'administrateur de l'école
        SchoolAdministrator admin = new SchoolAdministrator();
        admin.setMember(member);
        admin.setSchool(savedSchool);
        admin.setAssignedDate(LocalDateTime.now());
        admin.setIsActive(true);

        schoolAdminRepository.save(admin);

        return savedSchool;
    }

    @Override
    public School getSchoolById(Long id) {
        return schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));
    }

    @Override
    public List<School> getAllApprovedSchools() {
        return schoolRepository.findByStatus(SchoolStatus.APPROVED);
    }

    @Override
    @Transactional
    public School updateSchoolStatus(Long id, SchoolStatus status) {
        School school = getSchoolById(id);
        school.setStatus(status);
        return schoolRepository.save(school);
    }

    @Override
    @Transactional
    public School updateSchool(Long id, SchoolUpdateDTO schoolDTO) {
        School school = getSchoolById(id);

        // Mettre à jour les champs
        if (schoolDTO.getName() != null) {
            school.setName(schoolDTO.getName());
        }
        if (schoolDTO.getAddress() != null) {
            school.setAddress(schoolDTO.getAddress());
        }
        // Continuer pour tous les champs...

        return schoolRepository.save(school);
    }

    // Autres méthodes comme recherche par critères, etc.
}

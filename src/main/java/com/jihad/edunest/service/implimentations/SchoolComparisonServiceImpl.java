package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.entities.SchoolImage;
import com.jihad.edunest.repository.SchoolImageRepository;
import com.jihad.edunest.repository.SchoolRepository;
import com.jihad.edunest.service.SchoolComparisonService;
import com.jihad.edunest.web.vms.request.school.SchoolComparisonRequest;
import com.jihad.edunest.web.vms.responce.school.SchoolCompareDTO;
import com.jihad.edunest.web.vms.responce.school.SchoolComparisonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class SchoolComparisonServiceImpl implements SchoolComparisonService {
    private final SchoolRepository schoolRepository;
    private final SchoolImageRepository schoolImageRepository;

    @Transactional(readOnly = true)
    @Override
    public SchoolComparisonResponse compareSchools(SchoolComparisonRequest request) {
        if (request.getSchoolIds() == null || request.getSchoolIds().isEmpty()) {
            throw new IllegalArgumentException("Veuillez fournir au moins une école à comparer");
        }

        // Récupérer les écoles par leurs IDs
        List<School> schools = schoolRepository.findAllById(request.getSchoolIds());

        if (schools.size() != request.getSchoolIds().size()) {
            // Cela signifie que certaines écoles n'ont pas été trouvées
            Set<Long> foundIds = schools.stream().map(School::getId).collect(Collectors.toSet());
            Set<Long> missingIds = request.getSchoolIds().stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            throw new RuntimeException("Certaines écoles n'ont pas été trouvées: " + missingIds);
        }

        // Transformer les écoles en DTOs de comparaison
        List<SchoolCompareDTO> schoolDTOs = schools.stream()
                .map(this::mapToCompareDTO)
                .collect(Collectors.toList());

        // Construire la matrice de comparaison
        Map<String, List<String>> comparisonMatrix = buildComparisonMatrix(schools);

        return SchoolComparisonResponse.builder()
                .schools(schoolDTOs)
                .comparisonMatrix(comparisonMatrix)
                .build();
    }

    private SchoolCompareDTO mapToCompareDTO(School school) {
        Float averageRating = schoolRepository.getAverageRatingBySchoolId(school.getId());
        Integer reviewCount = schoolRepository.getReviewCountBySchoolId(school.getId());

        // Récupérer la première image (si disponible)
        Optional<SchoolImage> firstImage = schoolImageRepository.findFirstBySchoolIdOrderBySortOrder(school.getId());

        return SchoolCompareDTO.builder()
                .id(school.getId())
                .name(school.getName())
                .address(school.getAddress())
                .city(school.getCity())
                .postalCode(school.getPostalCode())
                .categoryName(school.getCategory() != null ? school.getCategory().getName() : "Non spécifié")
                .averageRating(averageRating != null ? averageRating : 0f)
                .reviewCount(reviewCount != null ? reviewCount : 0)
                .website(school.getWebsite())
                .phoneNumber(school.getPhoneNumber())
                .email(school.getEmail())
                .imageUrl(firstImage.map(SchoolImage::getUrl).orElse(null))
                .build();
    }

    private Map<String, List<String>> buildComparisonMatrix(List<School> schools) {
        Map<String, List<String>> matrix = new LinkedHashMap<>();

        // Critères à comparer
        matrix.put("Nom", schools.stream().map(School::getName).collect(Collectors.toList()));
        matrix.put("Adresse", schools.stream().map(School::getAddress).collect(Collectors.toList()));
        matrix.put("Ville", schools.stream().map(School::getCity).collect(Collectors.toList()));
        matrix.put("Code postal", schools.stream().map(School::getPostalCode).collect(Collectors.toList()));
        matrix.put("Téléphone", schools.stream().map(School::getPhoneNumber).collect(Collectors.toList()));
        matrix.put("Email", schools.stream().map(School::getEmail).collect(Collectors.toList()));
        matrix.put("Site web", schools.stream().map(School::getWebsite).collect(Collectors.toList()));

        // Catégorie
        matrix.put("Catégorie", schools.stream()
                .map(school -> school.getCategory() != null ? school.getCategory().getName() : "Non spécifié")
                .collect(Collectors.toList()));

        // Note moyenne
        matrix.put("Note moyenne", schools.stream()
                .map(school -> {
                    Float rating = schoolRepository.getAverageRatingBySchoolId(school.getId());
                    return rating != null ? String.format("%.1f/5", rating) : "Aucune note";
                })
                .collect(Collectors.toList()));

        // Nombre d'avis
        matrix.put("Nombre d'avis", schools.stream()
                .map(school -> {
                    Integer count = schoolRepository.getReviewCountBySchoolId(school.getId());
                    return count != null ? count.toString() : "0";
                })
                .collect(Collectors.toList()));

        return matrix;
    }
}

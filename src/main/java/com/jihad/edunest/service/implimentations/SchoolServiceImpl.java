package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.Review;
import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.entities.SchoolImage;
import com.jihad.edunest.repository.ReviewRepository;
import com.jihad.edunest.repository.SchoolImageRepository;
import com.jihad.edunest.repository.SchoolRepository;
import com.jihad.edunest.service.SchoolService;
import com.jihad.edunest.web.vms.request.school.SchoolSearchRequest;
import com.jihad.edunest.web.vms.responce.review.ReviewDTO;
import com.jihad.edunest.web.vms.responce.school.SchoolDetailsResponse;
import com.jihad.edunest.web.vms.responce.school.SchoolImageDTO;
import com.jihad.edunest.web.vms.responce.school.SchoolSearchResponse;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository schoolRepository;
    private final ReviewRepository reviewRepository;
    private final SchoolImageRepository schoolImageRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<SchoolSearchResponse> searchSchools(SchoolSearchRequest request) {
        int page = request.getPage() == null ? 0 : request.getPage();
        int size = request.getSize() == null ? 10 : request.getSize();

        String sortBy = request.getSortBy() == null ? "name" : request.getSortBy();
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(request.getSortDirection()) ?
                        Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy);

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<School> schools = schoolRepository.searchSchools(
                request.getName(),
                request.getCity(),
                request.getPostalCode(),
                request.getCategoryId(),
                pageable);

        return schools.map(this::mapToSearchResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public SchoolDetailsResponse getSchoolDetails(Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RuntimeException("École non trouvée avec l'ID: " + schoolId));

        Float averageRating = schoolRepository.getAverageRatingBySchoolId(schoolId);
        Integer reviewCount = schoolRepository.getReviewCountBySchoolId(schoolId);

        List<SchoolImage> images = schoolImageRepository.findBySchoolIdOrderBySortOrder(schoolId);
        List<Review> reviews = reviewRepository.findBySchoolIdOrderByCreatedAtDesc(schoolId);

        return mapToDetailsResponse(school, averageRating, reviewCount, images, reviews);
    }

    private SchoolSearchResponse mapToSearchResponse(School school) {
        Float averageRating = schoolRepository.getAverageRatingBySchoolId(school.getId());
        Integer reviewCount = schoolRepository.getReviewCountBySchoolId(school.getId());

        // Récupérer la première image (si disponible)
        Optional<SchoolImage> firstImage = schoolImageRepository.findFirstBySchoolIdOrderBySortOrder(school.getId());

        return SchoolSearchResponse.builder()
                .id(school.getId())
                .name(school.getName())
                .address(school.getAddress())
                .city(school.getCity())
                .postalCode(school.getPostalCode())
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .categoryName(school.getCategory() != null ? school.getCategory().getName() : null)
                .imageUrl(firstImage.map(SchoolImage::getUrl).orElse(null))
                .build();
    }

    private SchoolDetailsResponse mapToDetailsResponse(
            School school,
            Float averageRating,
            Integer reviewCount,
            List<SchoolImage> images,
            List<Review> reviews) {

        List<SchoolImageDTO> imageDTOs = images.stream()
                .map(image -> SchoolImageDTO.builder()
                        .id(image.getId())
                        .url(image.getUrl())
                        .caption(image.getCaption())
                        .build())
                .collect(Collectors.toList());

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(review -> ReviewDTO.builder()
                        .id(review.getId())
                        .content(review.getContent())
                        .rating(review.getRating())
                        .createdAt(review.getCreatedAt())
                        .memberName(review.getMember() != null ?
                                review.getMember().getFirstName() + " " + review.getMember().getLastName() : "Anonyme")
                        .build())
                .collect(Collectors.toList());

        return SchoolDetailsResponse.builder()
                .id(school.getId())
                .name(school.getName())
                .address(school.getAddress())
                .city(school.getCity())
                .postalCode(school.getPostalCode())
                .phoneNumber(school.getPhoneNumber())
                .email(school.getEmail())
                .website(school.getWebsite())
                .description(school.getDescription())
                .categoryName(school.getCategory() != null ? school.getCategory().getName() : null)
                .averageRating(averageRating)
                .reviewCount(reviewCount)
                .images(imageDTOs)
                .reviews(reviewDTOs)
                .build();
    }
}

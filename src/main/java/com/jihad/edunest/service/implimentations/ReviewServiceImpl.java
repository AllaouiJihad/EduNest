package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.Member;
import com.jihad.edunest.domaine.entities.Review;
import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.repository.MemberRepository;
import com.jihad.edunest.repository.ReviewRepository;
import com.jihad.edunest.repository.SchoolRepository;
import com.jihad.edunest.service.ReviewService;
import com.jihad.edunest.web.vms.request.review.ReviewCreateRequest;
import com.jihad.edunest.web.vms.request.review.ReviewUpdateRequest;
import com.jihad.edunest.web.vms.responce.review.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final SchoolRepository schoolRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ReviewDTO createReview(ReviewCreateRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));

        School school = schoolRepository.findById(request.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found with ID: " + request.getSchoolId()));

        // Check if the member already reviewed this school
        if (reviewRepository.existsByMemberIdAndSchoolId(memberId, request.getSchoolId())) {
            throw new RuntimeException("Member has already reviewed this school");
        }

        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .member(member)
                .school(school)
                .build();

        Review savedReview = reviewRepository.save(review);
        return mapToDTO(savedReview);
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long reviewId, ReviewUpdateRequest request, Long memberId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));

        // Verify ownership
        if (!review.getMember().getId().equals(memberId)) {
            throw new RuntimeException("You do not have permission to update this review");
        }

        review.setContent(request.getContent());
        review.setRating(request.getRating());

        Review updatedReview = reviewRepository.save(review);
        return mapToDTO(updatedReview);
    }

    @Override
    @Transactional
    public boolean deleteReview(Long reviewId, Long memberId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));

        // Verify ownership (or admin permission could be added here)
        if (!review.getMember().getId().equals(memberId)) {
            throw new RuntimeException("You do not have permission to delete this review");
        }

        reviewRepository.delete(review);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> getReviewsBySchool(Long schoolId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> reviews = reviewRepository.findBySchoolIdOrderByCreatedAtDesc(schoolId, pageable);
        return reviews.map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> getReviewsByMember(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Review> reviews = reviewRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable);
        return reviews.map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));
        return mapToDTO(review);
    }

    private ReviewDTO mapToDTO(Review review) {
        String memberName = "Anonyme";
        if (review.getMember() != null) {
            memberName = review.getMember().getFirstName() + " " + review.getMember().getLastName();
        }

        return ReviewDTO.builder()
                .id(review.getId())
                .content(review.getContent())
                .rating(review.getRating())
                .createdAt(review.getCreatedAt())
                .memberName(memberName)
                .schoolId(review.getSchool().getId())
                .schoolName(review.getSchool().getName())
                .build();
    }
}

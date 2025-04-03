package com.jihad.edunest.web.controller;

import com.jihad.edunest.service.ReviewService;
import com.jihad.edunest.web.vms.request.review.ReviewCreateRequest;
import com.jihad.edunest.web.vms.request.review.ReviewUpdateRequest;
import com.jihad.edunest.web.vms.responce.review.ReviewDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewCreateRequest request) {
        Long memberId = getCurrentMemberId();
        ReviewDTO createdReview = reviewService.createReview(request, memberId);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequest request) {
        Long memberId = getCurrentMemberId();
        ReviewDTO updatedReview = reviewService.updateReview(reviewId, request, memberId);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        Long memberId = getCurrentMemberId();
        boolean deleted = reviewService.deleteReview(reviewId, memberId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<Page<ReviewDTO>> getReviewsBySchool(
            @PathVariable Long schoolId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ReviewDTO> reviews = reviewService.getReviewsBySchool(schoolId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/member/me")
    public ResponseEntity<Page<ReviewDTO>> getMyReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long memberId = getCurrentMemberId();
        Page<ReviewDTO> reviews = reviewService.getReviewsByMember(memberId, page, size);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long reviewId) {
        ReviewDTO review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    private Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Cette partie dépend de votre implémentation de l'authentification
        // Si vous utilisez un UserDetailsService personnalisé qui charge Member
        if (authentication != null && authentication.getPrincipal() instanceof com.jihad.edunest.domaine.entities.Member) {
            return ((com.jihad.edunest.domaine.entities.Member) authentication.getPrincipal()).getId();
        }
        throw new RuntimeException("User not authenticated or not a valid member");
    }
}

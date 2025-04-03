package com.jihad.edunest.service;

import com.jihad.edunest.web.vms.request.review.ReviewCreateRequest;
import com.jihad.edunest.web.vms.request.review.ReviewUpdateRequest;
import com.jihad.edunest.web.vms.responce.review.ReviewDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {

    /**
     * Create a new review for a school
     *
     * @param request Contains review details and school information
     * @param memberId ID of the member creating the review
     * @return The created review
     */
    ReviewDTO createReview(ReviewCreateRequest request, Long memberId);

    /**
     * Update an existing review
     *
     * @param reviewId Review ID to update
     * @param request Updated review data
     * @param memberId Member ID to verify ownership
     * @return The updated review
     */
    ReviewDTO updateReview(Long reviewId, ReviewUpdateRequest request, Long memberId);

    /**
     * Delete a review
     *
     * @param reviewId Review ID to delete
     * @param memberId Member ID to verify ownership
     * @return true if deleted successfully
     */
    boolean deleteReview(Long reviewId, Long memberId);

    /**
     * Get reviews for a school
     *
     * @param schoolId School ID
     * @param page Page number
     * @param size Page size
     * @return Page of reviews
     */
    Page<ReviewDTO> getReviewsBySchool(Long schoolId, int page, int size);

    /**
     * Get reviews created by a member
     *
     * @param memberId Member ID
     * @param page Page number
     * @param size Page size
     * @return Page of reviews
     */
    Page<ReviewDTO> getReviewsByMember(Long memberId, int page, int size);

    /**
     * Get a specific review by ID
     *
     * @param reviewId Review ID
     * @return Review details
     */
    ReviewDTO getReviewById(Long reviewId);
}

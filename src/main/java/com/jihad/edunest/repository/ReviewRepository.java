package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    /**
     * Find reviews by school ID sorted by creation date
     */
    List<Review> findBySchoolIdOrderByCreatedAtDesc(Long schoolId);

    /**
     * Find reviews by school ID with pagination
     */
    Page<Review> findBySchoolIdOrderByCreatedAtDesc(Long schoolId, Pageable pageable);

    /**
     * Find reviews by member ID with pagination
     */
    Page<Review> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    /**
     * Check if a member has already reviewed a school
     */
    boolean existsByMemberIdAndSchoolId(Long memberId, Long schoolId);

    /**
     * Get average rating for a school
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.school.id = :schoolId")
    Float getAverageRatingBySchoolId(@Param("schoolId") Long schoolId);

    /**
     * Count reviews for a school
     */
    @Query("SELECT COUNT(r) FROM Review r WHERE r.school.id = :schoolId")
    Integer getReviewCountBySchoolId(@Param("schoolId") Long schoolId);
}

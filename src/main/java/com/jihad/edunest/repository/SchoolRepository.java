package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School,Long> {
    List<School> findByCategoryId(Long categoryId);

    List<School> findByCity(String city);

    @Query("SELECT s FROM School s WHERE s.name LIKE %?1% OR s.description LIKE %?1%")
    List<School> findByNameOrDescriptionContaining(String keyword);

    @Query("SELECT s FROM School s JOIN s.reviews r GROUP BY s.id ORDER BY AVG(r.rating) DESC")
    List<School> findTopRatedSchools();
    @Query("SELECT s FROM School s " +
            "LEFT JOIN s.category c " +
            "WHERE (:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:city IS NULL OR LOWER(s.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
            "AND (:postalCode IS NULL OR s.postalCode = :postalCode) " +
            "AND (:categoryId IS NULL OR c.id = :categoryId)")
    Page<School> searchSchools(
            @Param("name") String name,
            @Param("city") String city,
            @Param("postalCode") String postalCode,
            @Param("categoryId") Long categoryId,
            Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.school.id = :schoolId")
    Float getAverageRatingBySchoolId(@Param("schoolId") Long schoolId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.school.id = :schoolId")
    Integer getReviewCountBySchoolId(@Param("schoolId") Long schoolId);
    Long countByCategoryId(Long categoryId);

}

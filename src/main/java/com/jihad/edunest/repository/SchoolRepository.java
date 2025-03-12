package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.School;
import com.jihad.edunest.domaine.enums.SchoolStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School,Long> {
    List<School> findByCategoryId(Long categoryId);

    List<School> findByCity(String city);

    @Query("SELECT s FROM School s WHERE s.name LIKE %?1% OR s.description LIKE %?1%")
    List<School> findByNameOrDescriptionContaining(String keyword);

    @Query("SELECT s FROM School s JOIN s.reviews r GROUP BY s.id ORDER BY AVG(r.rating) DESC")
    List<School> findTopRatedSchools();

}

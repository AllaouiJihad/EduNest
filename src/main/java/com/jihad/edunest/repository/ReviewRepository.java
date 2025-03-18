package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findBySchoolIdOrderByCreatedAtDesc(Long schoolId);
}

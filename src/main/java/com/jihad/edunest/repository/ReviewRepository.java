package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}

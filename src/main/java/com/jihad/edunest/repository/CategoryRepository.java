package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByType(String type);
    Optional<Category> findByName(String name);
    List<Category> findByActiveTrue();
    List<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveTrue(
            String name, String description);
}

package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByType(String type);
    List<Category> findByActiveTrue();
}

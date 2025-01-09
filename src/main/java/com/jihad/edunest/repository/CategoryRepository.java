package com.jihad.edunest.repository;

import com.jihad.edunest.domaine.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}

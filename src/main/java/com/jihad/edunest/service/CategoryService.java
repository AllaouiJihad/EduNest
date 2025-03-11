package com.jihad.edunest.service;

import com.jihad.edunest.domaine.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Category save(Category category);
    void deleteById(Long id);
    List<Category> findByType(String type);
    Optional<Category> findByName(String name);
    List<Category> findAllActive();
}

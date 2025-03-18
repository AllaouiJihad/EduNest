package com.jihad.edunest.service;

import com.jihad.edunest.domaine.entities.Category;
import com.jihad.edunest.web.vms.request.category.CategoryRequest;
import com.jihad.edunest.web.vms.responce.category.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<CategoryResponse> getAllActiveCategories();
    Page<CategoryResponse> getAllCategories(int page, int size, String sortBy, String sortDir);
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    CategoryResponse toggleCategoryStatus(Long id, boolean active);
    void deleteCategory(Long id);

}

package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.Category;
import com.jihad.edunest.repository.CategoryRepository;
import com.jihad.edunest.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> findAllActive() {
        return categoryRepository.findByActiveTrue();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Vérifier s'il y a des écoles associées à cette catégorie
        categoryRepository.findById(id).ifPresent(category -> {
            if (category.getSchools() != null && !category.getSchools().isEmpty()) {
                // Si des écoles utilisent cette catégorie, nous la désactivons au lieu de la supprimer
                category.setActive(false);
                categoryRepository.save(category);
            } else {
                // Si aucune école n'utilise cette catégorie, nous pouvons la supprimer en toute sécurité
                categoryRepository.deleteById(id);
            }
        });
    }

    @Override
    public List<Category> findByType(String type) {
        return categoryRepository.findByType(type);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Transactional
    public Category createCategory(String name, String type, String description) {
        // Vérifier si une catégorie avec ce nom existe déjà
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent()) {
            throw new IllegalArgumentException("Une catégorie avec ce nom existe déjà");
        }

        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setDescription(description);
        category.setActive(true);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, String name, String type, String description, Boolean active) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id: " + id));

        // Vérifier si le nouveau nom n'est pas déjà utilisé par une autre catégorie
        if (name != null && !name.equals(category.getName())) {
            categoryRepository.findByName(name).ifPresent(c -> {
                if (!c.getId().equals(id)) {
                    throw new IllegalArgumentException("Une catégorie avec ce nom existe déjà");
                }
            });
            category.setName(name);
        }

        if (type != null) {
            category.setType(type);
        }

        if (description != null) {
            category.setDescription(description);
        }

        if (active != null) {
            category.setActive(active);
        }

        return categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public List<Category> searchCategories(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAllActive();
        }

        return categoryRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndActiveTrue(
                keyword, keyword);
    }
}

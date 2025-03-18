package com.jihad.edunest.service.implimentations;

import com.jihad.edunest.domaine.entities.Category;
import com.jihad.edunest.repository.CategoryRepository;
import com.jihad.edunest.repository.SchoolRepository;
import com.jihad.edunest.service.CategoryService;
import com.jihad.edunest.web.vms.request.category.CategoryRequest;
import com.jihad.edunest.web.vms.responce.category.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final SchoolRepository schoolRepository;

    /**
     * Récupère toutes les catégories actives
     * @return Liste des catégories actives
     */
    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponse> getAllActiveCategories() {
        return categoryRepository.findByActive(true).stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère toutes les catégories (actives et inactives) avec pagination
     * @param page Numéro de page
     * @param size Taille de la page
     * @param sortBy Champ de tri
     * @param sortDir Direction de tri
     * @return Page de catégories
     */
    @Transactional(readOnly = true)
    @Override
    public Page<CategoryResponse> getAllCategories(int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return categoryRepository.findAll(pageable)
                .map(this::mapToCategoryResponse);
    }

    /**
     * Récupère une catégorie par son ID
     * @param id ID de la catégorie
     * @return Catégorie trouvée
     * @throws RuntimeException si la catégorie n'existe pas
     */
    @Transactional(readOnly = true)
    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + id));

        return mapToCategoryResponse(category);
    }

    /**
     * Crée une nouvelle catégorie
     * @param request Données de la catégorie
     * @return Catégorie créée
     */
    @Transactional
    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        // Vérifier si une catégorie avec le même nom existe déjà
        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        Category category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .active(request.getActive())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(savedCategory);
    }

    /**
     * Met à jour une catégorie existante
     * @param id ID de la catégorie
     * @param request Nouvelles données
     * @return Catégorie mise à jour
     * @throws RuntimeException si la catégorie n'existe pas
     */
    @Transactional
    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + id));

        // Vérifier si une autre catégorie avec le même nom existe déjà
        if (!category.getName().equalsIgnoreCase(request.getName()) &&
                categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà");
        }

        category.setName(request.getName());
        category.setType(request.getType());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive());

        Category updatedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(updatedCategory);
    }

    /**
     * Active ou désactive une catégorie
     * @param id ID de la catégorie
     * @param active Statut d'activation
     * @return Catégorie mise à jour
     * @throws RuntimeException si la catégorie n'existe pas
     */
    @Transactional
    @Override
    public CategoryResponse toggleCategoryStatus(Long id, boolean active) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + id));

        category.setActive(active);
        Category updatedCategory = categoryRepository.save(category);
        return mapToCategoryResponse(updatedCategory);
    }

    /**
     * Supprime une catégorie si elle n'est pas utilisée
     * @param id ID de la catégorie
     * @throws RuntimeException si la catégorie est utilisée par des écoles
     */
    @Transactional
    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + id));

        Long schoolCount = schoolRepository.countByCategoryId(id);
        if (schoolCount > 0) {
            throw new RuntimeException("Impossible de supprimer cette catégorie car elle est utilisée par " + schoolCount + " école(s)");
        }

        categoryRepository.delete(category);
    }

    /**
     * Convertit une entité Category en DTO CategoryResponse
     * @param category Entité à convertir
     * @return DTO correspondant
     */
    private CategoryResponse mapToCategoryResponse(Category category) {
        Long schoolCount = schoolRepository.countByCategoryId(category.getId());

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .description(category.getDescription())
                .active(category.getActive())
                .schoolCount(schoolCount.intValue())
                .build();
    }
}

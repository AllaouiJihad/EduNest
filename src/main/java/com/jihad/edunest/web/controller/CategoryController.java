package com.jihad.edunest.web.controller;

import com.jihad.edunest.service.CategoryService;
import com.jihad.edunest.web.vms.request.category.CategoryRequest;
import com.jihad.edunest.web.vms.responce.category.CategoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Récupère toutes les catégories actives
     * @return Liste des catégories actives
     */
    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getAllActiveCategories() {
        return ResponseEntity.ok(categoryService.getAllActiveCategories());
    }

    /**
     * Récupère toutes les catégories avec pagination
     * @param page Numéro de page (par défaut 0)
     * @param size Taille de la page (par défaut 10)
     * @param sortBy Champ de tri (par défaut "name")
     * @param sortDir Direction de tri (par défaut "asc")
     * @return Page de catégories
     */
    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(categoryService.getAllCategories(page, size, sortBy, sortDir));
    }

    /**
     * Récupère une catégorie par son ID
     * @param id ID de la catégorie
     * @return Catégorie trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    /**
     * Crée une nouvelle catégorie (réservé aux administrateurs)
     * @param request Données de la catégorie
     * @return Catégorie créée
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return new ResponseEntity<>(categoryService.createCategory(request), HttpStatus.CREATED);
    }

    /**
     * Met à jour une catégorie existante (réservé aux administrateurs)
     * @param id ID de la catégorie
     * @param request Nouvelles données
     * @return Catégorie mise à jour
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    /**
     * Active ou désactive une catégorie (réservé aux administrateurs)
     * @param id ID de la catégorie
     * @param active Statut d'activation
     * @return Catégorie mise à jour
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> toggleCategoryStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {

        return ResponseEntity.ok(categoryService.toggleCategoryStatus(id, active));
    }

    /**
     * Supprime une catégorie si elle n'est pas utilisée (réservé aux administrateurs)
     * @param id ID de la catégorie
     * @return Réponse vide avec statut 204
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

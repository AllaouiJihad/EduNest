package com.jihad.edunest.web.vms.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    @Size(max = 255, message = "Le nom de la catégorie ne doit pas dépasser 255 caractères")
    private String name;

    @NotBlank(message = "Le type de la catégorie est obligatoire")
    @Size(max = 255, message = "Le type de la catégorie ne doit pas dépasser 255 caractères")
    private String type;

    @Size(max = 255, message = "La description ne doit pas dépasser 255 caractères")
    private String description;

    private Boolean active = true;
}

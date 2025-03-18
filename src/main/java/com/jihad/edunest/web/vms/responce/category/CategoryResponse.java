package com.jihad.edunest.web.vms.responce.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String type;
    private String description;
    private Boolean active;
    private Integer schoolCount; // Nombre d'écoles associées à cette catégorie
}

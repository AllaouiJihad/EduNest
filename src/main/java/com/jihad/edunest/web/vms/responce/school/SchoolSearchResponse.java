package com.jihad.edunest.web.vms.responce.school;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolSearchResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String postalCode;
    private Float averageRating;
    private Integer reviewCount;
    private String categoryName;
    private String imageUrl; // L'URL de la première image de l'école
}

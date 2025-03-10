package com.jihad.edunest.web.vms.responce;

import com.jihad.edunest.domaine.enums.SchoolStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchoolResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String postalCode;
    private String city;
    private String phoneNumber;
    private String email;
    private String website;
    private String description;
    private SchoolStatus status;
    private CategoryDTO category;
    private Double averageRating;
    private int favoriteCount;
    // autres champs pertinents
}


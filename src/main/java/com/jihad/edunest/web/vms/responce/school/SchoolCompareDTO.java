package com.jihad.edunest.web.vms.responce.school;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolCompareDTO {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String postalCode;
    private String categoryName;
    private Float averageRating;
    private Integer reviewCount;
    private String website;
    private String phoneNumber;
    private String email;
    private String imageUrl; // URL de l'image principale de l'école
}

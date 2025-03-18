package com.jihad.edunest.web.vms.responce.school;

import com.jihad.edunest.web.vms.responce.review.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDetailsResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String postalCode;
    private String phoneNumber;
    private String email;
    private String website;
    private String description;
    private String categoryName;
    private Float averageRating;
    private Integer reviewCount;
    private List<SchoolImageDTO> images;
    private List<ReviewDTO> reviews;
}

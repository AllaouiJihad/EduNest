package com.jihad.edunest.web.vms.request.school;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolSearchRequest {
    private String city;
    private String name;
    private Long categoryId;
    private String postalCode;
    private Float minRating;
    private Float maxRating;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortDirection;
}

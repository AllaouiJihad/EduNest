package com.jihad.edunest.web.vms.responce.school;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteSchoolDTO {
    private Long id;
    private Long schoolId;
    private String schoolName;
    private String schoolCity;
    private String categoryName;
    private String notes;
    private LocalDateTime addedDate;
}

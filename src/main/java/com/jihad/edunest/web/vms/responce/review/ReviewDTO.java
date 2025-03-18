package com.jihad.edunest.web.vms.responce.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private String content;
    private Float rating;
    private LocalDateTime createdAt;
    private String memberName;
}

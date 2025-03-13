package com.jihad.edunest.web.vms.responce.school;

import com.jihad.edunest.domaine.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolRegistrationResponseDto {
    private Long id;
    private String schoolName;
    private RequestStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private String adminReviewNotes;
}

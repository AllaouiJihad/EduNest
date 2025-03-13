package com.jihad.edunest.web.vms.request.school;

import com.jihad.edunest.domaine.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolRegistrationReviewDto {

    @NotNull(message = "L'identifiant de la demande est obligatoire")
    private Long requestId;

    @NotNull(message = "Le statut est obligatoire")
    private RequestStatus status;

    private String adminReviewNotes;
}

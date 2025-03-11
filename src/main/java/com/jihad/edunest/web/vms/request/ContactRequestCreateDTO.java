package com.jihad.edunest.web.vms.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestCreateDTO {
    @NotBlank(message = "Le sujet est obligatoire")
    private String subject;

    @NotBlank(message = "Le message est obligatoire")
    private String message;
}

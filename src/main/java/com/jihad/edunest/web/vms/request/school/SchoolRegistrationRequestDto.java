package com.jihad.edunest.web.vms.request.school;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolRegistrationRequestDto {

    @NotBlank(message = "Le nom de l'école est obligatoire")
    private String schoolName;

    @NotBlank(message = "L'adresse est obligatoire")
    private String schoolAddress;

    @NotBlank(message = "Le code postal est obligatoire")
    private String schoolPostalCode;

    @NotBlank(message = "La ville est obligatoire")
    private String schoolCity;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String schoolPhoneNumber;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String schoolEmail;

    private String schoolWebsite;

    @NotBlank(message = "La description est obligatoire")
    private String schoolDescription;

    @NotNull(message = "La catégorie est obligatoire")
    private Long categoryId;

    private String additionalInfo;
}


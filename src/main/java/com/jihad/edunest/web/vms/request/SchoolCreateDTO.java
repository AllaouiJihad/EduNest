package com.jihad.edunest.web.vms.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SchoolCreateDTO {
    @NotBlank(message = "School name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    private String postalCode;

    @NotBlank(message = "City is required")
    private String city;

    private String phoneNumber;

    @Email(message = "Please provide a valid email address")
    private String email;

    private String website;

    private String description;

    private Long categoryId;
}


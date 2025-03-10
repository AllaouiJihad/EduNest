package com.jihad.edunest.web.vms.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ForgotPasswordVM {
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;
}

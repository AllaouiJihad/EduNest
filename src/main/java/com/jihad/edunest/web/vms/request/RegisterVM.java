package com.jihad.edunest.web.vms.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterVM {
    @Email(message = "Email should be valid.")
    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter.")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter.")
    @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least one digit.")
    private String password;


    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;


    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be valid.")
    private String phone;


}

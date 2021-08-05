package com.forum.dto;

import com.forum.dto.validator.PasswordMatches;
import lombok.*;

import javax.validation.constraints.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordMatches
public class UserSignupDto {
    @NotEmpty
    @NotNull
    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 20, message = "Cannot be less than 3 characters")
    private String userName;

    @NotEmpty
    @NotNull
    @Email(regexp = ".+@.+\\..+", message = "Not a valid email")
    private String email;

    @NotEmpty
    @NotNull
    @Size(min = 6, message = "Cannot be less than 6 characters")
    private String password;
    private String matchingPassword;
}

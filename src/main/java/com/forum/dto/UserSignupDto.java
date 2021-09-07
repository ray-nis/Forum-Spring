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
    @NotBlank(message = "{validationUsernameRequired}")
    @Size(min = 3, max = 20, message = "{validationUsernameCharLimit}")
    private String userName;

    @NotBlank(message = "{validationEmailRequired}")
    @Email(regexp = ".+@.+\\..+", message = "{validationValidEmail}")
    private String email;

    @NotBlank(message = "{validationPasswordRequired}")
    @Size(min = 6, message = "{validationPasswordMinCharacters}")
    private String password;
    private String matchingPassword;
}

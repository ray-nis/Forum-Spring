package com.forum.dto;

import com.forum.dto.validator.PasswordChangeDtoMatches;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PasswordChangeDtoMatches
public class PasswordChangeDto {
    @NotBlank(message = "{validationPasswordRequired}")
    @Size(min = 6, message = "{validationPasswordMinCharacters}")
    private String oldPassword;

    @NotBlank(message = "{validationPasswordRequired}")
    @Size(min = 6, message = "{validationPasswordMinCharacters}")
    private String newPassword;
    private String confirmPassword;
}

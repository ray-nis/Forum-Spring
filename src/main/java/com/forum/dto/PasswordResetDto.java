package com.forum.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetDto {
    @NotBlank(message = "{validationEmailRequired}")
    @Email(regexp = ".+@.+\\..+", message = "{validationValidEmail}")
    private String email;
}

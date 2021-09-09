package com.forum.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailChangeDto {
    @NotBlank(message = "{validationEmailRequired}")
    @Email(regexp = ".+@.+\\..+", message = "{validationValidEmail}")
    private String email;

    @NotBlank(message = "{validationPasswordRequired}")
    @Size(min = 6, message = "{validationPasswordMinCharacters}")
    private String password;
}

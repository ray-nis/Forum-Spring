package com.forum.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordDto {
    @NotBlank(message = "{validationPasswordRequired}")
    @Size(min = 6, message = "{validationPasswordMinCharacters}")
    private String password;
}

package com.forum.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordDto {
    @NotEmpty(message = "{validationPasswordRequired}")
    @NotNull
    @Size(min = 6, message = "{validationPasswordMinCharacters}")
    private String password;
}

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
public class ContactDto {
    @NotBlank(message = "{validationEmailRequired}")
    @Email(regexp = ".+@.+\\..+", message = "{validationValidEmail}")
    private String email;

    @NotBlank(message = "{validationPostRequired}")
    @Size(min = 3, max = 1000, message = "{validationPostCharacters}")
    private String content;
}

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
public class UsernameChangeDto {
    @NotBlank(message = "{validationUsernameRequired}")
    @Size(min = 3, max = 20, message = "{validationUsernameCharLimit}")
    private String userName;

    @NotBlank(message = "{validationPasswordRequired}")
    @Size(min = 6, message = "{validationPasswordMinCharacters}")
    private String password;
}

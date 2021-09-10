package com.forum.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostChangeDto {
    @NotBlank(message = "{validationPostRequired}")
    @Size(min = 3, message = "{validationPostCharacters}")
    private String postContent;
}

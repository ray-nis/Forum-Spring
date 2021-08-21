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
public class PostDto {
    @NotEmpty
    @NotNull
    @NotBlank(message = "Title is required.")
    @Size(min = 3, max = 50, message = "Cannot be less than 3 or bigger than 50 characters")
    String title;

    @NotEmpty
    @NotNull
    @NotBlank(message = "Post is required.")
    @Size(min = 3, message = "Cannot be less than 3 characters")
    String postContent;
}

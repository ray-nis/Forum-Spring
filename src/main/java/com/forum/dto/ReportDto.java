package com.forum.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {
    private String reportReason;

    @Size(max = 30, message = "{validationPostCharacters}")
    private String reportComment;
}

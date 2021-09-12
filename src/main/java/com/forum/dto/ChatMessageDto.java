package com.forum.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private String content;
    private Long recipientId;
}

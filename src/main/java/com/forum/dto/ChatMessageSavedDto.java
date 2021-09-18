package com.forum.dto;

import lombok.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageSavedDto {
    private String content;
    private String senderUsername;
    private Instant createdAt;
}

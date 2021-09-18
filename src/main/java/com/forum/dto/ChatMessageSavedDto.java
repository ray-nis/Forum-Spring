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

    public String getSentTime() {
        ZonedDateTime sentZDT = getCreatedAt().atZone(ZoneId.of("UTC"));
        ZonedDateTime todayZDT = ZonedDateTime.now(ZoneId.of("UTC"));

        if (sentZDT.truncatedTo(ChronoUnit.DAYS).equals(todayZDT.truncatedTo(ChronoUnit.DAYS))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return sentZDT.format(formatter);
        }

        if (sentZDT.truncatedTo(ChronoUnit.YEARS).equals(todayZDT.truncatedTo(ChronoUnit.YEARS))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM");
            return sentZDT.format(formatter);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm - dd/MM/yy");
        return sentZDT.format(formatter);
    }
}

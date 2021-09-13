package com.forum.dto;

import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;

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
        Locale locale = LocaleContextHolder.getLocale();
        PrettyTime p = new PrettyTime(locale);
        return p.format(Date.from(getCreatedAt()));
    }
}

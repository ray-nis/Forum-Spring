package com.forum.model;

import com.forum.model.audit.DateAudit;
import com.forum.model.enums.MessageStatus;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChatMessage extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User sender;

    private String content;

    @ManyToOne
    private ChatRoom room;

    public String getSentTime() {
        Locale locale = LocaleContextHolder.getLocale();
        PrettyTime p = new PrettyTime(locale);
        return p.format(Date.from(getCreatedAt()));
    }
}

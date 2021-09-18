package com.forum.model;

import com.forum.model.audit.DateAudit;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    @Column(length = 1000)
    private String content;

    @ManyToOne
    private ChatRoom room;
}

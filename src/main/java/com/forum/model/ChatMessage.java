package com.forum.model;

import com.forum.model.audit.DateAudit;
import com.forum.model.enums.MessageStatus;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

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
}

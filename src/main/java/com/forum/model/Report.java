package com.forum.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String reportReason;

    private String reportComment;

    @ManyToOne
    @JoinColumn()
    private User reporter;

    @ManyToOne
    @JoinColumn()
    private Post post;
}

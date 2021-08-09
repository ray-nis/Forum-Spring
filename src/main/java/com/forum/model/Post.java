package com.forum.model;

import com.forum.model.audit.DateAudit;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Post extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User poster;

    private String postContent;

    @ManyToOne
    private Category category;

    @Override
    public String toString() {
        return postContent + " posted by " + poster.getUsername();
    }
}

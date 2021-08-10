package com.forum.model;

import com.forum.model.audit.DateAudit;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Column(nullable = false)
    private User poster;

    @Size(min = 3)
    @NotEmpty
    @NotNull
    private String postContent;

    @ManyToOne
    private Category category;

    @Override
    public String toString() {
        return postContent + " posted by " + poster.getUsername();
    }
}

package com.forum.model;

import com.forum.model.audit.DateAudit;
import com.forum.util.SlugUtil;
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
    @NotNull
    private User poster;

    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    private String slug;

    @Size(min = 3)
    @NotEmpty
    @NotNull
    private String postContent;

    @ManyToOne
    private Category category;

    @PrePersist
    public void slugColumn() {
        slug = SlugUtil.toSlug(title);
    }
}

package com.forum.model;

import com.forum.model.audit.DateAudit;
import com.forum.util.SlugUtil;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Category extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String slug;

    @Size(max = 50)
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();

    @PrePersist
    public void slugColumn() {
        slug = SlugUtil.toSlug(name);
    }
}

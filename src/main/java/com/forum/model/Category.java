package com.forum.model;

import com.forum.model.audit.DateAudit;
import lombok.*;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();
}

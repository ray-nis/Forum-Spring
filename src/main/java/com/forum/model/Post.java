package com.forum.model;

import com.forum.model.audit.DateAudit;
import com.forum.util.SlugUtil;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

    private Boolean pinned = false;

    @Size(min = 3)
    @NotEmpty
    @NotNull
    private String postContent;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private List<Comment> commentList;

    @ManyToOne
    private Category category;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_favorites", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<User> usersFavorited;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_likes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<User> usersLiked;

    private Integer timesViewed = 0;

    public String getPostedTime() {
        Locale locale = LocaleContextHolder.getLocale();
        PrettyTime p = new PrettyTime(locale);
        return p.format(Date.from(getCreatedAt()));
    }

    @PrePersist
    public void slugColumn() {
        slug = SlugUtil.toSlug(title);
    }
}

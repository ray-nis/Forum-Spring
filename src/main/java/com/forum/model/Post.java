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
import java.util.Locale;

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

package com.forum.model;

import com.forum.model.audit.DateAudit;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends DateAudit implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 20)
    @Column(unique=true)
    private String userName;

    @NotBlank(message = "Email is required.")
    @Email(regexp = ".+@.+\\..+")
    @Column(unique=true)
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 6)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private Boolean nonLocked = true;

    private Boolean enabled = false;

    @OneToMany(mappedBy = "poster")
    private List<Post> posts;

    @OneToMany(mappedBy = "commenter")
    private List<Comment> comments;

    @ManyToMany(mappedBy = "usersFavorited")
    private Set<Post> favoritePosts;

    @ManyToMany(mappedBy = "usersLiked")
    private Set<Post> likedPosts;

    @OneToMany(mappedBy = "reporter", cascade = CascadeType.ALL)
    private List<Report> reportList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return grantedAuthorities;
    }

    public String getVisibleUsername() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!userName.equals(user.userName)) return false;
        if (!email.equals(user.email)) return false;
        if (!password.equals(user.password)) return false;
        if (nonLocked != null ? !nonLocked.equals(user.nonLocked) : user.nonLocked != null) return false;
        return enabled != null ? enabled.equals(user.enabled) : user.enabled == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + userName.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User [username = " + getUsername() + "]";
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

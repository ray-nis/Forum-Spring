package com.forum.util;

import com.forum.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserUtil {
    public User getUser() {
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}

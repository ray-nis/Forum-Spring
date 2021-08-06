package com.forum.event;

import com.forum.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    private String contextPath;

    public RegistrationCompleteEvent(User user, String contextPath) {
        super(user);
        this.user = user;
        this.contextPath = contextPath;
    }
}

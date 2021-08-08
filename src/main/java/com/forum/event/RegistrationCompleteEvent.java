package com.forum.event;

import com.forum.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {
    private User user;
    private String baseUrl;

    public RegistrationCompleteEvent(User user, String baseUrl) {
        super(user);
        this.user = user;
        this.baseUrl = baseUrl;
    }
}

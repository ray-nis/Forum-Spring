package com.forum.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/* Class may seem redundant but it's for testing purposes */
@Component
public class ClockUtil {
    public final static int verificationTokenExpirationMins = 30;

    public LocalDateTime getTimeNow() {
        return LocalDateTime.now();
    }
}

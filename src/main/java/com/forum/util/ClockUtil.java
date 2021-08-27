package com.forum.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClockUtil {
    public final static int verificationTokenExpirationMins = 30;
    public final static int passwordResetTokenExpirationMins = 15;

    public LocalDateTime getTimeNow() {
        return LocalDateTime.now();
    }
}

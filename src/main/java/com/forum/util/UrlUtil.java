package com.forum.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {
    public final static String passwordResetTokenUrl = "/resetPassword?token=";
    public final static String verificationTokenUrl = "/registrationConfirm?token=";

    public static String getUrlFromServletRequest(HttpServletRequest request) {
        String url = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        return url;
    }
}

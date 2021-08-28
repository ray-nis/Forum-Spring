package com.forum.event;

import com.forum.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountLockedException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String error = "error";
        /*if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            error = "error2";
        }
        if (exception.getClass().isAssignableFrom(DisabledException.class)) {
            error = "error3";
        }
        if (exception.getClass().isAssignableFrom(LockedException.class)) {
            error = "error4";
        }*/

        super.setDefaultFailureUrl(UrlUtil.loginFailureRedirectUrl + error);

        super.onAuthenticationFailure(request, response, exception);
    }
}

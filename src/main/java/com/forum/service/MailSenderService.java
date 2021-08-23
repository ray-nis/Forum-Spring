package com.forum.service;

import com.forum.exception.MailSenderException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

    public void sendAccountVerificationEmail(String recipientAddress, String confirmationUrl) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("hello@gmail.com");
        email.setTo(recipientAddress);
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage("accountVerificationSubject", null, locale);
        email.setSubject(message);
        email.setText(confirmationUrl);
        try {
            mailSender.send(email);
        } catch (RuntimeException err) {
            throw new MailSenderException();
        }
    }
}

package com.forum.service;

import com.forum.exception.MailSenderException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService {
    private final JavaMailSender mailSender;

    public void sendAccountVerificationEmail(String recipientAddress, String confirmationUrl) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("hello@gmail.com");
        email.setTo(recipientAddress);
        email.setSubject("Registration Confirmation");
        email.setText(confirmationUrl);
        try {
            mailSender.send(email);
        } catch (RuntimeException err) {
            throw new MailSenderException();
        }
    }
}

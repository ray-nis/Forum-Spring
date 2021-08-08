package com.forum.event;

import com.forum.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class RegistrationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final VerificationTokenService verificationTokenService;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent registrationCompleteEvent) {
        this.confirmRegistration(registrationCompleteEvent);
    }

    @Async
    private void confirmRegistration(RegistrationCompleteEvent event) {
        String token = verificationTokenService.createVerificationToken(event.getUser());

        String recipientAddress = event.getUser().getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getBaseUrl() + "/registrationConfirm?token=" + token;

        // TODO refactor as a service
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("hello@gmail.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(confirmationUrl);
        try {
            mailSender.send(email);
        } catch (RuntimeException err) {
            log.error(String.valueOf(err));
        }
    }
}

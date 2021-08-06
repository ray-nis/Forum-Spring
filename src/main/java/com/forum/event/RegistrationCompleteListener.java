package com.forum.event;

import com.forum.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final VerificationTokenService verificationTokenService;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent registrationCompleteEvent) {
        this.confirmRegistration(registrationCompleteEvent);
    }

    private void confirmRegistration(RegistrationCompleteEvent event) {
        String token = verificationTokenService.createVerificationToken(event.getUser());

        String recipientAddress = event.getUser().getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getContextPath() + "/registrationConfirm?token=" + token;

        // TODO refactor as a service
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("hello@gmail.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("\n" + "http://localhost:8080" + confirmationUrl);
        log.info(email.toString());
        try {
            mailSender.send(email);
        } catch (RuntimeException err) {
            log.error(String.valueOf(err));
        }
    }
}

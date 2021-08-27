package com.forum.service;

import com.forum.model.PasswordResetToken;
import com.forum.model.User;
import com.forum.repository.PasswordResetRepository;
import com.forum.util.ClockUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetRepository passwordResetRepository;
    private final UserService userService;
    private final MailSenderService mailSenderService;
    private final ClockUtil clockUtil;

    public void resetPassword(String email, String baseUrl) {
        Optional<User> user = userService.findUserByEmailIgnoreCase(email);
        if (user.isEmpty()) return;
        String token = createPasswordResetToken(user.get());
        String confirmationUrl = baseUrl + "/resetPasswordToken?token=" + token;
        mailSenderService.sendResetPasswordEmail(user.get().getEmail(), confirmationUrl);
    }

    @Transactional
    public String createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .createdAt(clockUtil.getTimeNow())
                .expiresAt(clockUtil.getTimeNow().plusMinutes(clockUtil.passwordResetTokenExpirationMins))
                .build();
        passwordResetRepository.save(passwordResetToken);
        return token;
    }

    public boolean isTokenValid(String token) {
        Optional<PasswordResetToken> passwordResetToken = getToken(token);
        if (passwordResetToken.isEmpty()) return false;
        if (passwordResetToken.get().getExpiresAt().isBefore(clockUtil.getTimeNow())) return false;
        return true;
    }

    public Optional<PasswordResetToken> getToken(String token) {
        return passwordResetRepository.findByToken(token);
    }
}

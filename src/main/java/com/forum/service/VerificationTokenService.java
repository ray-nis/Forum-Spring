package com.forum.service;

import com.forum.model.User;
import com.forum.model.VerificationToken;
import com.forum.repository.VerificationTokenRepository;
import com.forum.util.ClockUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserService userService;
    private final ClockUtil clockUtil;

    @Transactional
    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .user(user)
                .token(token)
                .createdAt(clockUtil.getTimeNow())
                .expiresAt(clockUtil.getTimeNow().plusMinutes(clockUtil.verificationTokenExpirationMins))
                .build();
        verificationTokenRepository.save(verificationToken);
        return verificationToken.getToken();
    }

    @Transactional
    public void confirmToken(VerificationToken verificationToken) {
        verificationToken.setConfirmedAt(clockUtil.getTimeNow());
        userService.enableUser(verificationToken.getUser());
    }

    public Optional<VerificationToken> getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}

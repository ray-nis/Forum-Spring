package com.forum.service;

import com.forum.model.User;
import com.forum.model.VerificationToken;
import com.forum.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserService userService;

    @Transactional
    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .user(user)
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        verificationTokenRepository.save(verificationToken);
        return verificationToken.getToken();
    }

    @Transactional
    public void confirmToken(VerificationToken verificationToken) {
        verificationToken.setConfirmedAt(LocalDateTime.now());
        userService.enableUser(verificationToken.getUser());
    }

    public Optional<VerificationToken> getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}

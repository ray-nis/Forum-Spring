package com.forum.service;

import com.forum.model.User;
import com.forum.model.VerificationToken;
import com.forum.repository.VerificationTokenRepository;
import com.forum.util.ClockUtil;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationTokenServiceTest {

    @Mock
    private VerificationTokenRepository verificationTokenRepository;
    @Mock
    private UserService userService;
    @Mock
    private ClockUtil clockUtil;
    @InjectMocks
    private VerificationTokenService verificationTokenService;

    private User user;
    private LocalDateTime dateTime;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .userName("username")
                .password("username")
                .enabled(false)
                .build();

        dateTime = LocalDateTime.of(2020, 12, 12, 15, 15, 15);
        when(clockUtil.getTimeNow()).thenReturn(dateTime);
    }

    @Test
    @DisplayName("Creates token and checks the token current date, expiry date and the associated user")
    void shouldReturnToken() {
        ArgumentCaptor<VerificationToken> captor = ArgumentCaptor.forClass(VerificationToken.class);
        when(verificationTokenRepository.save(any(VerificationToken.class))).thenReturn(null);
        verificationTokenService.createVerificationToken(user);

        verify(verificationTokenRepository).save(captor.capture());
        VerificationToken vTokenCreated = captor.getValue();

        assertThat(vTokenCreated.getCreatedAt()).isEqualTo(dateTime);
        assertThat(vTokenCreated.getExpiresAt()).isEqualTo(dateTime.plusMinutes(clockUtil.verificationTokenExpirationMins));
        assertThat(vTokenCreated.getUser()).isEqualTo(user);
    }

    @Test
    void shouldConfirmAtCorrectTime() {
        VerificationToken token = VerificationToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(clockUtil.getTimeNow().plusDays(2))
                .expiresAt(clockUtil.getTimeNow().plusDays(2))
                .build();

        verificationTokenService.confirmToken(token);
        assertThat(token.getConfirmedAt()).isEqualTo(dateTime);
    }
}
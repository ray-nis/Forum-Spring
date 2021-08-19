package com.forum.controller.v1;

import com.forum.model.User;
import com.forum.model.VerificationToken;
import com.forum.service.UserDetailsServiceImpl;
import com.forum.service.UserService;
import com.forum.service.VerificationTokenService;
import com.forum.util.ClockUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private ApplicationEventPublisher eventPublisher;
    @MockBean
    private VerificationTokenService verificationTokenService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private ClockUtil clockUtil;

    @Test
    void shouldReturnLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnSignUp() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signUp"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void shouldReturnBadTokenWithNullToken() throws Exception {
        when(verificationTokenService.getVerificationToken("")).thenReturn(Optional.empty());

        mockMvc.perform(get("/registrationConfirm?token=2"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/badToken"))
                .andExpect(model().attribute("msg", "Bad token"));
    }

    @Test
    void shouldReturnTokenExpiredWithExpiredToken() throws Exception {
        VerificationToken token = VerificationToken.builder()
                .id(1L)
                .token("thetoken")
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now())
                .user(new User())
                .build();

        when(verificationTokenService.getVerificationToken("2")).thenReturn(Optional.of(token));
        when(clockUtil.getTimeNow()).thenReturn(LocalDateTime.now().plusDays(10));

        mockMvc.perform(get("/registrationConfirm?token=2"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/badToken"))
                .andExpect(model().attribute("msg", "Token expired"));
    }

    @Test
    void shouldReturnBadTokenWithConfirmedToken() throws Exception {
        VerificationToken token = VerificationToken.builder()
                .id(1L)
                .token("thetoken")
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now())
                .confirmedAt(LocalDateTime.now().plusSeconds(30))
                .user(new User())
                .build();

        when(verificationTokenService.getVerificationToken("2")).thenReturn(Optional.of(token));
        when(clockUtil.getTimeNow()).thenReturn(LocalDateTime.now().minusDays(10));

        mockMvc.perform(get("/registrationConfirm?token=2"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/badToken"))
                .andExpect(model().attribute("msg", "Bad token"));
    }

    @Test
    void shouldRedirectLoginWithGoodToken() throws Exception {
        VerificationToken token = VerificationToken.builder()
                .id(1L)
                .token("thetoken")
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now())
                .user(new User())
                .build();

        when(verificationTokenService.getVerificationToken("2")).thenReturn(Optional.of(token));
        when(clockUtil.getTimeNow()).thenReturn(LocalDateTime.now().minusDays(10));

        mockMvc.perform(get("/registrationConfirm?token=2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
package com.forum.controller.v1;

import com.forum.exception.UserExistsException;
import com.forum.model.User;
import com.forum.model.VerificationToken;
import com.forum.service.*;
import com.forum.util.ClockUtil;
import com.forum.util.UrlUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @MockBean
    private AuthenticationFailureHandler loginFailureHandler;
    @MockBean
    private PasswordResetService passwordResetService;
    @MockBean
    private MailSenderService mailSenderService;

    @Test
    void shouldReturnLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldContainUsernameValidationError() throws Exception {
        mockMvc.perform(post("/signup")
                .param("userName", "Rr")
                .param("email", "username@gmail.com")
                .param("password", "password")
                .param("matchingPassword", "password")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signUp"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("user", "userName"));
    }

    @Test
    void shouldContainEmailValidationError() throws Exception {
        mockMvc.perform(post("/signup")
                .param("userName", "username")
                .param("email", "usernamegmail.com")
                .param("password", "password")
                .param("matchingPassword", "password")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signUp"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }

    @Test
    void shouldContainShortPasswordValidationError() throws Exception {
        mockMvc.perform(post("/signup")
                .param("userName", "username")
                .param("email", "username@gmail.com")
                .param("password", "pass")
                .param("matchingPassword", "pass")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signUp"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("user", "password"));
    }

    @Test
    void shouldContainMismatchingPasswordValidationError() throws Exception {
        mockMvc.perform(post("/signup")
                .param("userName", "username")
                .param("email", "username@gmail.com")
                .param("password", "password")
                .param("matchingPassword", "password1")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signUp"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().hasErrors());
    }

    @Test
    void shouldShowUserExists() throws Exception {
        doThrow(new UserExistsException("emailExists"))
                .when(userService).saveUser(any());

        mockMvc.perform(post("/signup")
                .param("userName", "username")
                .param("email", "username@gmail.com")
                .param("password", "password")
                .param("matchingPassword", "password")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signUp"))
                .andExpect(model().attributeExists("err"));
    }

    @Test
    void shouldShowSuccesfulSignUp() throws Exception {
        User user = User.builder()
                .id(1L)
                .userName("username")
                .password("password")
                .email("email@email.com")
                .enabled(false)
                .nonLocked(true)
                .build();
        when(userService.saveUser(any())).thenReturn(user);

        mockMvc.perform(post("/signup")
                .param("userName", "username")
                .param("email", "username@gmail.com")
                .param("password", "password")
                .param("matchingPassword", "password")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/successfulSignUp"));
    }

    @Test
    void shouldReturnSignUp() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/signUp"))
                .andExpect(model().attributeExists("user"));
    }

    /*

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
    */
}
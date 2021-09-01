package com.forum.controller.v1;

import com.forum.model.User;
import com.forum.service.UserDetailsServiceImpl;
import com.forum.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProfileController.class)
@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private AuthenticationFailureHandler loginFailureHandler;


    @Test
    @WithMockUser
    void shouldGetProfileById() throws Exception {
        User user = User.builder()
                .id(1L)
                .userName("username")
                .password("password")
                .email("email@email.com")
                .enabled(false)
                .nonLocked(true)
                .build();
        when(userService.findUserWithPostsById(any())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/profile/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/profile"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser
    void shouldThrowNotFound() throws Exception {
        when(userService.findUserWithPostsById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/profile/1"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException));
    }


}
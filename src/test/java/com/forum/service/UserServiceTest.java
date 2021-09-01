package com.forum.service;

import com.forum.dto.UserSignupDto;
import com.forum.exception.UserExistsException;
import com.forum.model.User;
import com.forum.repository.RoleRepository;
import com.forum.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldEnableUser() {
        User user = User.builder()
                .id(1L)
                .userName("username")
                .password("username")
                .enabled(false)
                .build();

        userService.enableUser(user);
        assertThat(user.getEnabled()).isTrue();
    }

    @Test
    void shouldThrowUserExistsByEmail () {
        User user = User.builder()
                .id(1L)
                .userName("username")
                .password("username")
                .email("username")
                .enabled(false)
                .build();

        when(userRepository.findByEmailIgnoreCase("username")).thenReturn(Optional.of(user));

        UserSignupDto userDto = UserSignupDto.builder().email("username").build();

        assertThatThrownBy(() -> {
            assertThat(userService.saveUser(userDto));
        }).isInstanceOf(UserExistsException.class)
        .hasMessageContaining("emailExists");
    }

    @Test
    void shouldThrowUserExistsByUsername () {
        User user = User.builder()
                .id(1L)
                .userName("username")
                .password("username")
                .email("username")
                .enabled(false)
                .build();

        when(userRepository.findByUserNameIgnoreCase("username")).thenReturn(Optional.of(user));

        UserSignupDto userDto = UserSignupDto.builder().email("email@email.com").userName("username").build();

        assertThatThrownBy(() -> {
            assertThat(userService.saveUser(userDto));
        }).isInstanceOf(UserExistsException.class)
                .hasMessageContaining("usernameExists");
    }
}
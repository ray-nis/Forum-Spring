package com.forum.repository;

import com.forum.model.Post;
import com.forum.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .userName("username")
                .password("username")
                .email("username@username.com")
                .enabled(false)
                .build();
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
    }

    @Test
    void shouldExistUser() {
        Optional<User> userRetrieved = userRepository.findById(1L);
        assertThat(userRetrieved.isPresent()).isTrue();
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(userRetrieved.get()).isEqualTo(user);
    }
}
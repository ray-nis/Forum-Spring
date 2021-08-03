package com.forum.util.dev;

import com.forum.model.Role;
import com.forum.model.User;
import com.forum.repository.RoleRepository;
import com.forum.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

@Profile("dev")
@Component
@RequiredArgsConstructor
@Slf4j
public class DataIntializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role userRole = Role.builder().role("ROLE_USER").build();
        Role adminRole = Role.builder().role("ROLE_ADMIN").build();
        roleRepository.save(userRole);
        roleRepository.save(adminRole);

        User john = User.builder()
                .userName("John")
                .email("john123@gmail.com")
                .enabled(true)
                .nonLocked(true)
                .password(passwordEncoder.encode("password"))
                .roles(new HashSet<Role>(Arrays.asList(userRole))).build();

        User ben = User.builder()
                .userName("Ben")
                .email("ben123@gmail.com")
                .enabled(true)
                .nonLocked(true)
                .password(passwordEncoder.encode("password"))
                .roles(new HashSet<Role>(Arrays.asList(userRole, adminRole))).build();

        userRepository.save(john);
        userRepository.save(ben);
    }
}

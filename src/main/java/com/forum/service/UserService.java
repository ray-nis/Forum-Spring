package com.forum.service;

import com.forum.dto.UserSignupDto;
import com.forum.exception.UserExistsException;
import com.forum.model.Role;
import com.forum.model.User;
import com.forum.repository.RoleRepository;
import com.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Transactional
    public User saveUser(UserSignupDto userSignupDto) throws UserExistsException {
        if (emailExists(userSignupDto.getEmail())) {
            throw new UserExistsException("Email already in use");
        }
        if (userNameExists(userSignupDto.getUserName())) {
            throw new UserExistsException("Username already in use");
        }
        Role userRole = roleRepository.findByRole("ROLE_USER");

        User user = User.builder()
                .userName(userSignupDto.getUserName())
                .email(userSignupDto.getEmail())
                .password(passwordEncoder.encode(userSignupDto.getPassword()))
                .roles(new HashSet<Role>(Arrays.asList(userRole)))
                .enabled(false)
                .nonLocked(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public void enableUser(User user) {
        user.setEnabled(true);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmailIgnoreCase(email).isPresent();
    }

    private boolean userNameExists(String userName) {
        return userRepository.findByUserNameIgnoreCase(userName).isPresent();
    }
}

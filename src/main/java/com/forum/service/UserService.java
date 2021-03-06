package com.forum.service;

import com.forum.dto.UserSignupDto;
import com.forum.exception.ResourceNotFoundException;
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

    @Transactional
    public User saveUser(UserSignupDto userSignupDto) throws UserExistsException {
        if (emailExists(userSignupDto.getEmail())) {
            throw new UserExistsException("emailExists");
        }
        if (userNameExists(userSignupDto.getUserName())) {
            throw new UserExistsException("usernameExists");
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

    public boolean emailExists(String email) {
        return userRepository.findByEmailIgnoreCase(email).isPresent();
    }

    public boolean userNameExists(String userName) {
        return userRepository.findByUserNameIgnoreCase(userName).isPresent();
    }

    public User findUserWithPostsById(Long id) throws ResourceNotFoundException {
        return userRepository.findUserWithPostsById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public Optional<User> findUserByEmailIgnoreCase(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Transactional
    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Transactional
    public void changeUsername(User user, String userName) {
        user.setUserName(userName);
        userRepository.save(user);
    }

    @Transactional
    public void changeEmail(User user, String email) {
        user.setEmail(email);
        userRepository.save(user);
    }

    public User findUserById(Long id) throws ResourceNotFoundException {
        return userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public boolean passwordsMatch(String password, User user) {
        return passwordEncoder.matches(password, user.getPassword());
    }
}

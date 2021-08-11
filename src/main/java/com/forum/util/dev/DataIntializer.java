package com.forum.util.dev;

import com.forum.model.Category;
import com.forum.model.Post;
import com.forum.model.Role;
import com.forum.model.User;
import com.forum.repository.CategoryRepository;
import com.forum.repository.PostRepository;
import com.forum.repository.RoleRepository;
import com.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Profile("dev")
@Component
@RequiredArgsConstructor
@Slf4j
public class DataIntializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

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

        Category firstCategory = Category.builder()
                .description("First category descp")
                .name("First category")
                .build();

        Category secondCategory = Category.builder()
                .description("Second category descp")
                .name("Second category")
                .build();

        categoryRepository.saveAll(Arrays.asList(firstCategory, secondCategory));

        Post firstPost = Post.builder()
                .title("1st Post Hello")
                .postContent("First post for 1st category")
                .category(firstCategory)
                .poster(john)
                .build();

        Post secondPost = Post.builder()
                .title("2nd Post No problem")
                .postContent("Second post for 1st category")
                .category(firstCategory)
                .poster(ben)
                .build();

        Post thirdPost = Post.builder()
                .title("3rd Post for the 1st in the 2nd category")
                .postContent("First post for 2nd category")
                .category(secondCategory)
                .poster(ben)
                .build();

        Post fourthPost = Post.builder()
                .title("4th Post for the 2nd in 2nd category")
                .postContent("Second post for 2nd category")
                .category(secondCategory)
                .poster(ben)
                .build();

        postRepository.saveAll(Arrays.asList(firstPost, secondPost, thirdPost, fourthPost));
    }
}

package com.forum.util.dev;

import com.forum.model.*;
import com.forum.repository.*;
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
import java.util.Optional;

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
    private final CommentRepository commentRepository;

    @Override
    public void run(String... args) throws Exception {
        Role userRole = Role.builder().role("ROLE_USER").build();
        Role adminRole = Role.builder().role("ROLE_ADMIN").build();
        Role modRole = Role.builder().role("ROLE_MODERATOR").build();
        roleRepository.save(userRole);
        roleRepository.save(adminRole);
        roleRepository.save(modRole);

        User john = User.builder()
                .userName("John")
                .email("john123@gmail.com")
                .enabled(true)
                .nonLocked(true)
                .password(passwordEncoder.encode("password"))
                .roles(new HashSet<Role>(Arrays.asList(modRole))).build();

        User ben = User.builder()
                .userName("Ben")
                .email("ben123@gmail.com")
                .enabled(true)
                .nonLocked(true)
                .password(passwordEncoder.encode("password"))
                .favoritePosts(new HashSet<>())
                .roles(new HashSet<Role>(Arrays.asList(adminRole))).build();

        User ty = User.builder()
                .userName("Ty12")
                .email("ty@gmail.com")
                .enabled(true)
                .nonLocked(true)
                .password(passwordEncoder.encode("password"))
                .roles(new HashSet<Role>(Arrays.asList(userRole))).build();

        User rob = User.builder()
                .userName("Rob13")
                .email("rob@gmail.com")
                .enabled(true)
                .nonLocked(true)
                .password(passwordEncoder.encode("password"))
                .roles(new HashSet<Role>(Arrays.asList(userRole, adminRole))).build();

        userRepository.saveAll(Arrays.asList(john, ben, ty, rob));

        Category firstCategory = Category.builder()
                .description("First category descp")
                .name("First category")
                .build();

        Category secondCategory = Category.builder()
                .description("Second category descp")
                .name("Second category")
                .build();

        Category thirdCategory = Category.builder()
                .description("Third category descp")
                .name("Third category")
                .build();

        categoryRepository.saveAll(Arrays.asList(firstCategory, secondCategory, thirdCategory));

        Post firstPost = Post.builder()
                .title("1st Post Hello")
                .postContent("First post for 1st category")
                .category(firstCategory)
                .poster(john)
                .timesViewed(1)
                .pinned(false)
                .locked(true)
                .build();

        Post secondPost = Post.builder()
                .title("2nd Post No problem")
                .postContent("Second post for 1st category")
                .category(firstCategory)
                .poster(ben)
                .timesViewed(2)
                .pinned(false)
                .locked(false)
                .build();

        Post thirdPost = Post.builder()
                .title("3rd Post for the 1st in the 2nd category")
                .postContent("First post for 2nd category")
                .category(secondCategory)
                .poster(ben)
                .timesViewed(2)
                .pinned(false)
                .locked(false)
                .build();

        Post fourthPost = Post.builder()
                .title("4th Post for the 2nd in 2nd category")
                .postContent("Second post for 2nd category")
                .category(secondCategory)
                .poster(ben)
                .timesViewed(0)
                .pinned(false)
                .locked(false)
                .build();

        Post fifthPost = Post.builder()
                .title("H?????? ???? ?? ????")
                .postContent("Third post for 2nd category")
                .category(secondCategory)
                .poster(john)
                .timesViewed(5)
                .pinned(true)
                .locked(false)
                .build();

        Post sixthPost = Post.builder()
                .title("4th Post for the 2nd in 2nd category")
                .postContent("Second post for 2nd category")
                .category(secondCategory)
                .poster(ben)
                .timesViewed(0)
                .pinned(false)
                .locked(false)
                .build();

        for (int i = 0; i < 15; i++) {
            Post post = Post.builder()
                    .title("For loop title " + i)
                    .postContent("For loop content " + i)
                    .category(firstCategory)
                    .poster(i % 2 == 0 ? ben : john)
                    .timesViewed(0)
                    .pinned(false)
                    .locked(false)
                    .build();
            postRepository.save(post);
        }

        postRepository.saveAll(Arrays.asList(firstPost, secondPost, thirdPost, fourthPost, fifthPost, sixthPost));

        for (int i = 0; i < 50; i++) {
            Comment comment = Comment.builder()
                    .post(firstPost)
                    .commentContent("Content " + i)
                    .commenter(ben)
                    .build();
            commentRepository.save(comment);
        }
    }
}

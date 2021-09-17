package com.forum.controller.v1;

import com.forum.dto.EmailChangeDto;
import com.forum.dto.PasswordChangeDto;
import com.forum.dto.UsernameChangeDto;
import com.forum.exception.ResourceNotFoundException;
import com.forum.model.Post;
import com.forum.model.User;
import com.forum.service.CommentService;
import com.forum.service.PostService;
import com.forum.service.UserService;
import com.forum.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ProfileController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final CurrentUserUtil currentUserUtil;

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable("id") Long id, Model model) throws ResourceNotFoundException {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("posts", postService.getFiveRecentPostsFromUser(user));
        model.addAttribute("comments", commentService.getFiveRecentCommentsFromUser(user));
        model.addAttribute("nrOfPosts", postService.getNumberOfPostsFromUser(user));
        model.addAttribute("nrOfComments", commentService.getNumberOfCommentsFromUser(user));
        return "profile/profile";
    }

    @GetMapping("/favorites")
    public String getFavorites(Model model) {
        User user = currentUserUtil.getUser();
        List<Post> favorites = postService.getFavorites(user);
        model.addAttribute("posts", favorites);
        return "profile/favorites";
    }

    @GetMapping("/profile")
    public String getOwnProfile(Model model) throws ResourceNotFoundException {
        User user = currentUserUtil.getUser();
        model.addAttribute("user", user);
        model.addAttribute("posts", postService.getFiveRecentPostsFromUser(user));
        model.addAttribute("comments", commentService.getFiveRecentCommentsFromUser(user));
        model.addAttribute("nrOfPosts", postService.getNumberOfPostsFromUser(user));
        model.addAttribute("nrOfComments", commentService.getNumberOfCommentsFromUser(user));
        return "profile/profile";
    }

    @GetMapping("/editprofile")
    public String editProfile(Model model) throws ResourceNotFoundException {
        User user = currentUserUtil.getUser();
        model.addAttribute("user", user);
        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        model.addAttribute("usernameChangeDto", new UsernameChangeDto());
        model.addAttribute("emailChangeDto", new EmailChangeDto());
        return "profile/editProfile";
    }

    @PostMapping("/changepassword")
    public String changePassword(@Valid @ModelAttribute("passwordChangeDto") PasswordChangeDto passwordChangeDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        User user = currentUserUtil.getUser();

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("passwordChangeDto", passwordChangeDto);
            model.addAttribute("usernameChangeDto", new UsernameChangeDto());
            model.addAttribute("emailChangeDto", new EmailChangeDto());
            return "profile/editProfile";
        }

        if (!userService.passwordsMatch(passwordChangeDto.getOldPassword(), user)) {
            model.addAttribute("user", user);
            model.addAttribute("passwordChangeDto", passwordChangeDto);
            model.addAttribute("usernameChangeDto", new UsernameChangeDto());
            model.addAttribute("emailChangeDto", new EmailChangeDto());

            model.addAttribute("passwordError", "currentPasswordIncorrect");
            return "profile/editProfile";
        }

        userService.changePassword(user, passwordChangeDto.getNewPassword());

        redirectAttributes.addFlashAttribute("success", "passwordChangeSuccess");
        return "redirect:/editprofile";
    }

    @PostMapping("/changeusername")
    public String changeUsername(@Valid @ModelAttribute("usernameChangeDto") UsernameChangeDto usernameChangeDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        User user = currentUserUtil.getUser();

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
            model.addAttribute("usernameChangeDto", usernameChangeDto);
            model.addAttribute("emailChangeDto", new EmailChangeDto());
            return "profile/editProfile";
        }

        if (userService.userNameExists(usernameChangeDto.getUserName())) {
            model.addAttribute("user", user);
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
            model.addAttribute("usernameChangeDto", usernameChangeDto);
            model.addAttribute("emailChangeDto", new EmailChangeDto());

            model.addAttribute("usernameError", "usernameExists");
            return "profile/editProfile";
        }

        if (!userService.passwordsMatch(usernameChangeDto.getPassword(), user)) {
            model.addAttribute("user", user);
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
            model.addAttribute("usernameChangeDto", usernameChangeDto);
            model.addAttribute("emailChangeDto", new EmailChangeDto());

            model.addAttribute("usernamePasswordError", "currentPasswordIncorrect");
            return "profile/editProfile";
        }

        userService.changeUsername(user, usernameChangeDto.getUserName());

        redirectAttributes.addFlashAttribute("success", "usernameChangeSuccess");
        return "redirect:/editprofile";
    }

    @PostMapping("/changeemail")
    public String changeEmail(@Valid @ModelAttribute("emailChangeDto") EmailChangeDto emailChangeDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        User user = currentUserUtil.getUser();

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
            model.addAttribute("usernameChangeDto", new UsernameChangeDto());
            model.addAttribute("emailChangeDto", emailChangeDto);
            return "profile/editProfile";
        }

        if (userService.emailExists(emailChangeDto.getEmail())) {
            model.addAttribute("user", user);
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
            model.addAttribute("usernameChangeDto", new UsernameChangeDto());
            model.addAttribute("emailChangeDto", emailChangeDto);


            model.addAttribute("emailError", "emailExists");
            return "profile/editProfile";
        }

        if (!userService.passwordsMatch(emailChangeDto.getPassword(), user)) {
            model.addAttribute("user", user);
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
            model.addAttribute("usernameChangeDto", new UsernameChangeDto());
            model.addAttribute("emailChangeDto", emailChangeDto);

            model.addAttribute("emailPasswordError", "currentPasswordIncorrect");
            return "profile/editProfile";
        }

        userService.changeEmail(user, emailChangeDto.getEmail());

        redirectAttributes.addFlashAttribute("success", "emailChangeSuccess");
        return "redirect:/editprofile";
    }
}

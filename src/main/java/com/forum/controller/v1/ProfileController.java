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
    public String editProfile(Model model) {
        User user = currentUserUtil.getUser();

        model.addAttribute("user", user);
        if (!model.containsAttribute("passwordChangeDto")) {
            model.addAttribute("passwordChangeDto", new PasswordChangeDto());
        }
        if (!model.containsAttribute("usernameChangeDto")) {
            model.addAttribute("usernameChangeDto", new UsernameChangeDto());
        }
        if (!model.containsAttribute("emailChangeDto")) {
            model.addAttribute("emailChangeDto", new EmailChangeDto());
        }
        return "profile/editProfile";
    }

    @PostMapping("/changepassword")
    public String changePassword(@Valid @ModelAttribute("passwordChangeDto") PasswordChangeDto passwordChangeDto, BindingResult result, RedirectAttributes redirectAttributes) {
        User user = currentUserUtil.getUser();

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("passwordChangeDto", passwordChangeDto);
            return "redirect:/editprofile";
        }

        if (!userService.passwordsMatch(passwordChangeDto.getOldPassword(), user)) {
            redirectAttributes.addFlashAttribute("passwordChangeDto", passwordChangeDto);
            redirectAttributes.addFlashAttribute("passwordError", "currentPasswordIncorrect");
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
            redirectAttributes.addFlashAttribute("usernameChangeDto", usernameChangeDto);
            return "redirect:/editprofile";
        }

        if (userService.userNameExists(usernameChangeDto.getUserName())) {
            redirectAttributes.addFlashAttribute("usernameError", "usernameExists");
            redirectAttributes.addFlashAttribute("usernameChangeDto", usernameChangeDto);
            return "redirect:/editprofile";
        }

        if (!userService.passwordsMatch(usernameChangeDto.getPassword(), user)) {
            redirectAttributes.addFlashAttribute("usernameChangeDto", usernameChangeDto);
            redirectAttributes.addFlashAttribute("usernamePasswordError", "currentPasswordIncorrect");
            return "redirect:/editprofile";
        }

        userService.changeUsername(user, usernameChangeDto.getUserName());

        redirectAttributes.addFlashAttribute("success", "usernameChangeSuccess");
        return "redirect:/editprofile";
    }

    @PostMapping("/changeemail")
    public String changeEmail(@Valid @ModelAttribute("emailChangeDto") EmailChangeDto emailChangeDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        User user = currentUserUtil.getUser();

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("emailChangeDto", emailChangeDto);
            return "redirect:/editprofile";
        }

        if (userService.emailExists(emailChangeDto.getEmail())) {
            redirectAttributes.addFlashAttribute("emailChangeDto", emailChangeDto);
            redirectAttributes.addFlashAttribute("emailError", "emailExists");
            return "redirect:/editprofile";
        }

        if (!userService.passwordsMatch(emailChangeDto.getPassword(), user)) {
            redirectAttributes.addFlashAttribute("emailChangeDto", emailChangeDto);
            redirectAttributes.addFlashAttribute("emailPasswordError", "currentPasswordIncorrect");
            return "redirect:/editprofile";
        }

        userService.changeEmail(user, emailChangeDto.getEmail());

        redirectAttributes.addFlashAttribute("success", "emailChangeSuccess");
        return "redirect:/editprofile";
    }
}

package com.forum.controller.v1;

import com.forum.dto.PasswordChangeDto;
import com.forum.exception.ResourceNotFoundException;
import com.forum.model.User;
import com.forum.repository.UserRepository;
import com.forum.service.CommentService;
import com.forum.service.PostService;
import com.forum.service.UserService;
import com.forum.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

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
        return "profile/editProfile";
    }

    @PostMapping("/changepassword")
    public String changePassword(@Valid @ModelAttribute("passwordChangeDto") PasswordChangeDto passwordChangeDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        User user = currentUserUtil.getUser();

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("passwordChangeDto", passwordChangeDto);
            return "profile/editProfile";
        }

        if (!userService.passwordsMatch(passwordChangeDto.getOldPassword(), user)) {
            model.addAttribute("user", user);
            model.addAttribute("passwordChangeDto", passwordChangeDto);
            model.addAttribute("error", "currentPasswordIncorrect");
            return "profile/editProfile";
        }

        userService.changePassword(user, passwordChangeDto.getNewPassword());

        redirectAttributes.addFlashAttribute("success", "passwordChangeSuccess");
        return "redirect:/editprofile";
    }
}

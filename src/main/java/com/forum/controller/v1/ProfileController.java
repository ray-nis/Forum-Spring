package com.forum.controller.v1;

import com.forum.exception.ResourceNotFoundException;
import com.forum.model.User;
import com.forum.repository.UserRepository;
import com.forum.service.UserService;
import com.forum.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class ProfileController {
    private final UserService userService;
    private final CurrentUserUtil currentUserUtil;

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable("id") Long id, Model model) throws ResourceNotFoundException {
        User user = userService.findUserWithPostsById(id);
        model.addAttribute("user", user);
        return "profile/profile";
    }

    @GetMapping("/profile")
    public String getOwnProfile(Model model) throws ResourceNotFoundException {
        User user = userService.findUserWithPostsById(currentUserUtil.getUser().getId());
        model.addAttribute("user", user);
        return "profile/profile";
    }
}

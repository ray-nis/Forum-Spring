package com.forum.controller.v1;

import com.forum.model.User;
import com.forum.repository.UserRepository;
import com.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class ProfileController {
    private final UserService userService;

    @GetMapping("/profile/{id}")
    public String getProfile(@PathVariable("id") Long id, Model model) {
        Optional<User> user = userService.findUserWithPostsById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "profile/profile";
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}

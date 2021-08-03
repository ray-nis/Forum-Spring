package com.forum.controller.v1;

import com.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String logIn() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signup";
    }
}

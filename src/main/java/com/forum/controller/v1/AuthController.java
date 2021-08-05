package com.forum.controller.v1;

import com.forum.dto.UserSignupDto;
import com.forum.exception.UserExistsException;
import com.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String logIn() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("user", new UserSignupDto());
        return "signUp";
    }

    @PostMapping("/signup")
    public ModelAndView saveUser(@Valid @ModelAttribute("user") UserSignupDto userSignupDto, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("signUp", "user", userSignupDto);
            return mav;
        }
        try {
            userService.saveUser(userSignupDto);
        }
        catch (UserExistsException err) {
            ModelAndView mav = new ModelAndView("signUp", "user", userSignupDto);
            mav.addObject("message", err.getMessage());
            return mav;
        }

        return new ModelAndView("succesfulSignUp");
    }
}

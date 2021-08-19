package com.forum.controller.v1;

import com.forum.dto.UserSignupDto;
import com.forum.event.RegistrationCompleteEvent;
import com.forum.exception.UserExistsException;
import com.forum.model.User;
import com.forum.model.VerificationToken;
import com.forum.service.UserService;
import com.forum.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenService verificationTokenService;

    @GetMapping("/login")
    public String logIn() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("user", new UserSignupDto());
        return "auth/signUp";
    }

    @PostMapping("/signup")
    public ModelAndView saveUser(@Valid @ModelAttribute("user") UserSignupDto userSignupDto, BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView("auth/signUp", "user", userSignupDto);
            return mav;
        }
        try {
            User user = userService.saveUser(userSignupDto);
            String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                    .replacePath(null)
                    .build()
                    .toUriString();
            eventPublisher.publishEvent(new RegistrationCompleteEvent(user, baseUrl));
        }
        catch (UserExistsException err) {
            ModelAndView mav = new ModelAndView("auth/signUp", "user", userSignupDto);
            mav.addObject("message", err.getMessage());
            return mav;
        }
        catch (RuntimeException err) {
            // TODO
        }

        return new ModelAndView("auth/succesfulSignUp");
    }

    @GetMapping("/registrationConfirm")
    public ModelAndView confirmRegistration(@RequestParam("token") String token, Model model) {
        Optional<VerificationToken> verificationToken = verificationTokenService.getVerificationToken(token);

        if (verificationToken.isEmpty()) {
            ModelAndView mav = new ModelAndView("auth/badToken", "msg", "Bad token");
            return mav;
        }

        if (verificationToken.get().getExpiresAt().isBefore(LocalDateTime.now())) {
            ModelAndView mav = new ModelAndView("auth/badToken", "msg", "Token expired");
            return mav;
        }

        if (verificationToken.get().getConfirmedAt() != null) {
            ModelAndView mav = new ModelAndView("auth/badToken", "msg", "Bad token");
            return mav;
        }

        verificationTokenService.confirmToken(verificationToken.get());
        return new ModelAndView("redirect:/login");
    }
}

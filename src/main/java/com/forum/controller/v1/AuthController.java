package com.forum.controller.v1;

import com.forum.dto.PasswordDto;
import com.forum.dto.EmailDto;
import com.forum.dto.PostChangeDto;
import com.forum.dto.UserSignupDto;
import com.forum.event.RegistrationCompleteEvent;
import com.forum.exception.BadTokenException;
import com.forum.exception.UserExistsException;
import com.forum.model.PasswordResetToken;
import com.forum.model.User;
import com.forum.service.MailSenderService;
import com.forum.service.PasswordResetService;
import com.forum.service.UserService;
import com.forum.service.VerificationTokenService;
import com.forum.util.ClockUtil;
import com.forum.util.CurrentUserUtil;
import com.forum.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final VerificationTokenService verificationTokenService;
    private final PasswordResetService passwordResetService;
    private final ClockUtil clockUtil;
    private final MessageSource messageSource;
    private final MailSenderService mailSenderService;
    private final CurrentUserUtil currentUserUtil;

    @GetMapping("/login")
    public String logIn() {
        if (currentUserUtil.isAuthenticated()) {
            return "redirect:/";
        }
        return "auth/logIn";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        if (currentUserUtil.isAuthenticated()) {
            return "redirect:/";
        }
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
            String baseUrl = UrlUtil.getUrlFromServletRequest(request);
            eventPublisher.publishEvent(new RegistrationCompleteEvent(user, baseUrl));
        }
        catch (UserExistsException err) {
            ModelAndView mav = new ModelAndView("auth/signUp", "user", userSignupDto);
            String message = messageSource.getMessage(err.getMessage(), null, LocaleContextHolder.getLocale());
            mav.addObject("err", message);
            return mav;
        }

        return new ModelAndView("auth/successfulSignUp");
    }

    @GetMapping("/registrationConfirm")
    public ModelAndView confirmRegistration(@RequestParam("token") String token, Model model) throws BadTokenException {
        if (verificationTokenService.isTokenValid(token)) {
            verificationTokenService.confirmToken(verificationTokenService.getVerificationToken(token).get());
            return new ModelAndView("redirect:/login");
        }

        throw new BadTokenException();
    }

    @GetMapping("/resendverification")
    public String resendVerificationPage(Model model) {
        model.addAttribute("emailDto", new EmailDto());
        return "auth/verification/resendVerification";
    }

    @PostMapping("/resendverification")
    public String resendVerification(@Valid @ModelAttribute("emailtDto") EmailDto emailDto, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("emailDto", new EmailDto());
            return "auth/verification/resendVerification";
        }

        Optional<User> user = userService.findUserByEmailIgnoreCase(emailDto.getEmail());
        if (user.isPresent() && user.get().isEnabled() == false) {
            String token = verificationTokenService.createVerificationToken(user.get());
            String recipientAddress = user.get().getEmail();
            String confirmationUrl = UrlUtil.getUrlFromServletRequest(request) + UrlUtil.verificationTokenUrl + token;
            mailSenderService.sendAccountVerificationEmail(recipientAddress, confirmationUrl);
        }

        return "auth/verification/sentResendVerification";
    }

    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam("token") String token, Model model) throws BadTokenException {
        if (passwordResetService.isTokenValid(token)) {
            model.addAttribute("token", token);
            model.addAttribute("passwordDto", new PasswordDto());
            return "auth/forgotPassword/changePassword";
        }

        throw new BadTokenException();
    }

    @PostMapping("/resetPassword")
    public String changePassword(@Valid @ModelAttribute("passwordDto") PasswordDto passwordDto,BindingResult result, HttpServletRequest request, Model model) throws BadTokenException {
        if (result.hasErrors()) {
            model.addAttribute("passwordDto", passwordDto);
            return "auth/forgotPassword/changePassword";
        }

        String token = request.getParameter("token");
        if (passwordResetService.isTokenValid(token)) {
            PasswordResetToken passwordResetToken = passwordResetService.getToken(token).get();
            userService.changePassword(passwordResetToken.getUser(), passwordDto.getPassword());
            return "auth/forgotPassword/successfulPasswordReset";
        }

        throw new BadTokenException();
    }

    @GetMapping("/forgotpassword")
    public String forgotPasswordPage(Model model) {
        if (!model.containsAttribute("passwordResetDto")) {
            model.addAttribute("passwordResetDto", new EmailDto());
        }
        return "auth/forgotPassword/forgotPassword";
    }

    @PostMapping("/forgotpassword")
    public String forgotPassword(@Valid @ModelAttribute("passwordResetDto") EmailDto emailDto, BindingResult result, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordResetDto", result);
            redirectAttributes.addFlashAttribute( "passwordResetDto", emailDto);
            return "redirect/forgotpassword";
        }

        String baseUrl = UrlUtil.getUrlFromServletRequest(request);
        passwordResetService.resetPassword(emailDto.getEmail(), baseUrl);

        return "redirect:/forgotpasswordsent";
    }

    @GetMapping("/forgotpasswordsent")
    public String sentForgotEmail() {
        return "auth/forgotPassword/sentResetPassword";
    }
}

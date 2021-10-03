package com.forum.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MailSenderException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleMailSenderFailure() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/customError");
        String message = messageSource.getMessage("cantSendEmail", null, LocaleContextHolder.getLocale());
        mav.addObject("errorMsg", message);
        return mav;
    }

    @ExceptionHandler(BadTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBadToken() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/customError");
        String message = messageSource.getMessage("badToken", null, LocaleContextHolder.getLocale());
        mav.addObject("errorMsg", message);
        return mav;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/404");
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleForbidden() {
        return "error/403";
    }
}

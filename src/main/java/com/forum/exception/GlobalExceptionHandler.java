package com.forum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MailSenderException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleMailSenderFailure() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("error/customError");
        mav.addObject("errorMsg", "There was a problem sending the verification email, please contact support to resolve the issue");
        return mav;
    }
}

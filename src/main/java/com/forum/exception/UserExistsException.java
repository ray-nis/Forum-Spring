package com.forum.exception;

// TODO refactor into localized message with resource bundler
public class UserExistsException extends Exception {
    public UserExistsException(String message) {
        super(message);
    }
}

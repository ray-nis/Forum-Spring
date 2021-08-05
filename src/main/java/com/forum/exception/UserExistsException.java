package com.forum.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException() {
        super();
    }

    public UserExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserExistsException(final String message) {
        super(message);
    }

    public UserExistsException(final Throwable cause) {
        super(cause);
    }
}

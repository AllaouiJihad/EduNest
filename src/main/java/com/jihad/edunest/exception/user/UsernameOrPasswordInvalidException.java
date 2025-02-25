package com.jihad.edunest.exception.user;

public class UsernameOrPasswordInvalidException extends RuntimeException{
    public UsernameOrPasswordInvalidException(String message) {
        super(message);
    }
}

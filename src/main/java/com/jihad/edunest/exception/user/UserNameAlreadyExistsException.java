package com.jihad.edunest.exception.user;

public class UserNameAlreadyExistsException extends RuntimeException{
    public UserNameAlreadyExistsException(String message) {
        super(message);
    }
}

package com.jihad.edunest.exception.user;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String userNotFound) {
        super(userNotFound);
    }
}

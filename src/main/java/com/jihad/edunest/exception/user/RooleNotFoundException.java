package com.jihad.edunest.exception.user;

public class RooleNotFoundException extends RuntimeException{
    public RooleNotFoundException(String roleNotFound) {
        super(roleNotFound);
    }
}

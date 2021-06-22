package com.guilherme.redditclone.exception;

public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message){
        super(message);
    }
}

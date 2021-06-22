package com.guilherme.redditclone.exception;

public class RedditCloneException extends RuntimeException {
    public RedditCloneException(String message, Exception exception){
        super(message, exception);
    }

    public RedditCloneException(String message){
        super(message);
    }
}

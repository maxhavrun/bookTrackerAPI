package com.havrun.bookTrackerAPI.exception.auth;

public class UserNotFoundExcepption extends RuntimeException {
    public UserNotFoundExcepption(String message) {
        super(message);
    }
}

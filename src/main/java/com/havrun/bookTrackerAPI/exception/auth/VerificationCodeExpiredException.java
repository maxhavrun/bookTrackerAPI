package com.havrun.bookTrackerAPI.exception.auth;

public class VerificationCodeExpiredException extends RuntimeException {
    public VerificationCodeExpiredException(String message) {
        super(message);
    }
}

package com.havrun.bookTrackerAPI.exception.handlers;

import com.havrun.bookTrackerAPI.DTO.errors.ApiErrorResponseDTO;
import com.havrun.bookTrackerAPI.exception.auth.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleUsernameExists(UsernameAlreadyExistsException ex) {
        ApiErrorResponseDTO response = new ApiErrorResponseDTO("USERNAME_EXISTS", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleEmailExists(EmailAlreadyExistsException ex) {
        ApiErrorResponseDTO response = new ApiErrorResponseDTO("EMAIL_EXISTS", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleGeneralException(RuntimeException ex) {
        ApiErrorResponseDTO response = new ApiErrorResponseDTO("GENERAL_ERROR", "An error occurred");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(VerificationCodeExpiredException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleVerificationCodeExpired(VerificationCodeExpiredException ex) {
        ApiErrorResponseDTO response = new ApiErrorResponseDTO("VERIFICATION_CODE_EXPIRED", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleInvalidVerificationCode(InvalidVerificationCodeException ex) {
        ApiErrorResponseDTO response = new ApiErrorResponseDTO("INVALID_VERIFICATION_CODE", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundExcepption.class)
    public ResponseEntity<ApiErrorResponseDTO> handleUserNotFound(UserNotFoundExcepption ex) {
        ApiErrorResponseDTO response = new ApiErrorResponseDTO("USER_NOT_FOUND", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleInvalidRefreshToken(InvalidRefreshTokenException ex) {
        ApiErrorResponseDTO response = new ApiErrorResponseDTO("INVALID_REFRESH_TOKEN", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}

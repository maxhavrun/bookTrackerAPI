package com.havrun.bookTrackerAPI.Controllers;

import com.havrun.bookTrackerAPI.DTO.auth.LoginDTO;
import com.havrun.bookTrackerAPI.DTO.JwtResponse;
import com.havrun.bookTrackerAPI.DTO.RefreshTokenRequest;
import com.havrun.bookTrackerAPI.DTO.auth.SignUpDTO;
import com.havrun.bookTrackerAPI.DTO.auth.SignUpResponseDTO;
import com.havrun.bookTrackerAPI.DTO.auth.VerificationDTO;
import com.havrun.bookTrackerAPI.Services.AuthenticationService;
import com.havrun.bookTrackerAPI.Services.VerificationService;
import com.havrun.bookTrackerAPI.entity.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final VerificationService verificationService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> register(
            @Valid @RequestBody SignUpDTO request
    ){
        User user = authenticationService.signUp(request);

        SignUpResponseDTO responseDTO = SignUpResponseDTO.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole().name())
                .message("User registered successfully. Please verify your email.")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(
            @Valid @RequestBody VerificationDTO verificationDTO
            ){
        verificationService.verifyAccount(verificationDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Account verified successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticate(
            @RequestBody LoginDTO request
    ){
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    @PostMapping("/resend-verification-code")
    public ResponseEntity<?> resendVerificationCode(
            @Email(message = "Email should be valid") @RequestBody String email
    ){
        verificationService.resendVerificationCode(email);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Verification code sent successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(

    ){
        return null;
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
            @RequestBody RefreshTokenRequest token
    ){
        return ResponseEntity.ok(authenticationService.refreshToken(token));
    }

}

package com.havrun.bookTrackerAPI.Controllers;

import com.havrun.bookTrackerAPI.DTO.AuthenticationRequest;
import com.havrun.bookTrackerAPI.DTO.JwtResponse;
import com.havrun.bookTrackerAPI.DTO.RefreshTokenRequest;
import com.havrun.bookTrackerAPI.DTO.RegisterRequest;
import com.havrun.bookTrackerAPI.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
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

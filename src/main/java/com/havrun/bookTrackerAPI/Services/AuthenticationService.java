package com.havrun.bookTrackerAPI.Services;

import com.havrun.bookTrackerAPI.DTO.*;
import com.havrun.bookTrackerAPI.DTO.auth.LoginDTO;
import com.havrun.bookTrackerAPI.DTO.auth.RegistrationDTO;
import com.havrun.bookTrackerAPI.Repository.RefreshTokenRepository;
import com.havrun.bookTrackerAPI.Repository.UserRepository;
import com.havrun.bookTrackerAPI.Services.Tokens.JwtService;
import com.havrun.bookTrackerAPI.Services.Tokens.RefreshTokenService;
import com.havrun.bookTrackerAPI.entity.RefreshToken;
import com.havrun.bookTrackerAPI.entity.User.Role;
import com.havrun.bookTrackerAPI.entity.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    public JwtResponse register(RegistrationDTO request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        var accessToken = jwtService.generateToken(user);
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.toString())
                .build();
    }

    public JwtResponse authenticate(LoginDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        if( refreshTokenRepository.findByUserId(user.getId()).isPresent() ){
            refreshTokenRepository.delete(refreshTokenRepository.findByUserId(user.getId()).get());
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        var accessToken = jwtService.generateToken(user);
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest token) {
        return refreshTokenService.findByToken(token.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken ->
                        {
                            var user = userRepository.findByUsername(refreshToken.getUser().getUsername()).orElseThrow();
                            var accessToken = jwtService.generateToken(user);
                            return JwtResponse.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(token.getToken())
                                    .build();
                        }
                        )
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

}

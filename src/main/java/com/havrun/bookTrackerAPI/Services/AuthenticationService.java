package com.havrun.bookTrackerAPI.Services;

import com.havrun.bookTrackerAPI.DTO.*;
import com.havrun.bookTrackerAPI.Repository.RefreshTokenRepository;
import com.havrun.bookTrackerAPI.Repository.UserRepository;
import com.havrun.bookTrackerAPI.entity.RefreshToken;
import com.havrun.bookTrackerAPI.entity.Role;
import com.havrun.bookTrackerAPI.entity.User;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public JwtResponse register(RegisterRequest request) {
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
                .token(refreshToken.toString())
                .build();
    }

    public JwtResponse authenticate(AuthenticationRequest request) {
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
                .token(refreshToken.getToken())
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
                                    .token(token.getToken())
                                    .build();
                        }
                        )
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

}

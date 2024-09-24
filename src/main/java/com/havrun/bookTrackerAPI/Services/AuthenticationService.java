package com.havrun.bookTrackerAPI.Services;

import com.havrun.bookTrackerAPI.DTO.*;
import com.havrun.bookTrackerAPI.DTO.auth.LoginDTO;
import com.havrun.bookTrackerAPI.DTO.auth.SignUpDTO;
import com.havrun.bookTrackerAPI.Repository.RefreshTokenRepository;
import com.havrun.bookTrackerAPI.Repository.UserRepository;
import com.havrun.bookTrackerAPI.Services.Tokens.JwtService;
import com.havrun.bookTrackerAPI.entity.RefreshToken;
import com.havrun.bookTrackerAPI.entity.User.Role;
import com.havrun.bookTrackerAPI.entity.User.User;
import com.havrun.bookTrackerAPI.exception.auth.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    @Transactional
    public User signUp(SignUpDTO request) {

        if(userRepository.findByUsername(request.getUsername()).isPresent()){
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .verificationCode(emailService.generateVerificationCode())
                .verificationCodeExpiresAt(emailService.generateVerificationCodeExpiration())
                .accountEnabled(false)
                .accountNonLocked(true)
                .build();
        emailService.sendVerificationEmail(user);
        return userRepository.save(user);
    }

    @Transactional
    public JwtResponse signIn(LoginDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        var user = findUserByUsername(request.getUsername());

        deleteExistingRefreshToken(user.getId());

        RefreshToken refreshToken = jwtService.createRefreshToken(user.getUsername());

        var accessToken = jwtService.generateAccessToken(user);
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Transactional
    public JwtResponse refreshToken(@RequestBody RefreshTokenRequest token) {
        return jwtService.findByRefreshToken(token.getToken())
                .map(jwtService::verifyRefreshTokenExpiration)
                .map(refreshToken ->
                        {
                            var user = userRepository.findByUsername(refreshToken.getUser().getUsername()).orElseThrow();
                            var accessToken = jwtService.generateAccessToken(user);
                            return JwtResponse.builder()
                                    .accessToken(accessToken)
                                    .refreshToken(token.getToken())
                                    .build();
                        }
                        )
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundExcepption("User not found with username: " + username));
    }

    private void deleteExistingRefreshToken(Integer userId) {
        refreshTokenRepository.findByUserId(userId).ifPresent(refreshTokenRepository::delete);
    }

}

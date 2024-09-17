package com.havrun.bookTrackerAPI.Services;

import com.havrun.bookTrackerAPI.Repository.RefreshTokenRepository;
import com.havrun.bookTrackerAPI.Repository.UserRepository;
import com.havrun.bookTrackerAPI.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {

        var user = userRepository.findByUsername(username).orElseThrow();
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expireDate( new Date(System.currentTimeMillis() + 1000 * 120)) // 7 days
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpireDate().before(new Date())) {
            refreshTokenRepository.delete(token);
            throw  new RuntimeException(token.getToken() + " is expired");
        }
        return token;
    }

}

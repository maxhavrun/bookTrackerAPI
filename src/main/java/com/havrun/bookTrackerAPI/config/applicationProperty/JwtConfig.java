package com.havrun.bookTrackerAPI.config.applicationProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.jwt")
@Getter
public class JwtConfig {
    private String secretKey;
    private ExpirationTime expirationTime;

    @Getter
    @RequiredArgsConstructor
    public static class ExpirationTime {
        private Integer access;
        private Integer refresh;
    }
}

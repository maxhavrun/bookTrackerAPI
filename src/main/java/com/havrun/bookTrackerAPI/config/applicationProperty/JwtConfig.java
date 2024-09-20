package com.havrun.bookTrackerAPI.config.applicationProperty;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.jwt")
@Getter
@Setter
public class JwtConfig {
    private String secretKey;
    private ExpirationTime expirationTime;

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class ExpirationTime {
        private long access;
        private long refresh;
    }
}

package com.havrun.bookTrackerAPI.config.applicationProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailConfig {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private Properties properties;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Properties {
        private Mail mail;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Mail {
        private Smtp smtp;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Smtp {
        private boolean auth;
        private Starttls starttls;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Starttls {
        private boolean enable;
    }
}

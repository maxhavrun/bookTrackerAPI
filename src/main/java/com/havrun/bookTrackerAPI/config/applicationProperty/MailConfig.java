package com.havrun.bookTrackerAPI.config.applicationProperty;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Getter
public class MailConfig {

    private String host;
    private String port;
    private String username;
    private String password;
    private Properties properties;

    @Getter
    public static class Properties {
        private Mail mail;
    }

    @Getter
    public static class Mail {
        private Smtp smtp;
    }

    @Getter
    public static class Smtp {
        private boolean auth;
        private Starttls starttls;
    }

    @Getter
    public static class Starttls {
        private boolean enable;
    }
}

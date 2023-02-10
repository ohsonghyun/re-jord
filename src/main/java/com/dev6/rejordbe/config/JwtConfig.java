package com.dev6.rejordbe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("rejord.jwt")
@lombok.Getter
@lombok.Setter
public class JwtConfig {

    private String sortKey;
    private TokenPeriod accessToken;
    private TokenPeriod refreshToken;

    @lombok.Data
    public static class TokenPeriod {
        private int period;
    }
}

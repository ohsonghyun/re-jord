package com.dev6.rejordbe

import com.dev6.rejordbe.config.JwtConfig
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

/**
 * TestSecurityConfig
 */
@TestConfiguration
class TestSecurityConfig {
    // TODO SecurityConfig 와 동일하게 변경할 것! 어떻게 동기화 안 되나...?
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeRequests().antMatchers("/v1/**").permitAll();
        httpSecurity.authorizeRequests().anyRequest().authenticated();
        return httpSecurity.build();
    }

    @Bean
    JwtConfig jwtConfig() {
        def config = new JwtConfig()

        def accessTokenPeriod = new JwtConfig.TokenPeriod()
        accessTokenPeriod.setPeriod(1800000)
        config.setAccessToken(accessTokenPeriod)

        def refreshTokenPeriod = new JwtConfig.TokenPeriod()
        refreshTokenPeriod.setPeriod(2592000000)
        config.setAccessToken(refreshTokenPeriod)

        return config;
    }
}

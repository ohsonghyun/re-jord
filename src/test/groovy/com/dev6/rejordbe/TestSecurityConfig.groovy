package com.dev6.rejordbe


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
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeRequests().antMatchers("/v1/**").permitAll();
        httpSecurity.authorizeRequests().anyRequest().authenticated();
        return httpSecurity.build();

    }
}

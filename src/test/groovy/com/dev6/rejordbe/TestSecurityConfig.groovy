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
    // TODO SecurityConfig 와 동일하게 변경할 것! 어떻게 동기화 안 되나...?
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeRequests().antMatchers("/v1/**").permitAll();
        httpSecurity.authorizeRequests().anyRequest().authenticated();
        return httpSecurity.build();

    }
}

package com.dev6.rejordbe.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfig
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] SWAGGER = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs"
    };

    /**
     * 리소스별 접근제어 설정
     *
     * @param httpSecurity
     * @return {@code SecurityFilterChain}
     * @throws Exception
     */
    @Bean
    SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // TODO flowertaekk 접근제어설정
        httpSecurity.authorizeRequests().antMatchers("/v1/**").permitAll();

        // swagger
        httpSecurity.authorizeRequests().antMatchers(SWAGGER).permitAll();
        // /swagger

        // h2-console
        httpSecurity.authorizeRequests().requestMatchers(PathRequest.toH2Console()).permitAll();
        httpSecurity.headers().frameOptions().disable();
        // /h2-console

        httpSecurity.authorizeRequests().anyRequest().authenticated();
        return httpSecurity.build();
    }

    /**
     * 패스워드 암호화 객체
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

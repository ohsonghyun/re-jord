package com.dev6.rejordbe.config;

import com.dev6.rejordbe.presentation.controller.argumentResolver.LoggedInUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * WebMvcConfig: PROD
 */
@Profile("prod")
@Configuration
@lombok.RequiredArgsConstructor
public class ProdWebMvcConfig implements WebMvcConfigurer {

    /**
     * CORS설정
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 Origin에 오픈. 서버에서 필요에 따라 access 제어
        registry.addMapping("/v1/**")
                // TODO PROD 도메인 설정
                .allowedOrigins("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoggedInUserArgumentResolver());
    }
}

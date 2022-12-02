package com.dev6.rejordbe.config.id;

import com.dev6.rejordbe.application.id.IdGenerator;
import com.dev6.rejordbe.application.id.IdGeneratorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdConfig {
    /**
     * ID 생성기 빈 등록
     */
    @Bean
    IdGenerator idGenerator() {
        return new IdGeneratorImpl();
    }
}

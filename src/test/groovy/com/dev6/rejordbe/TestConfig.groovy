package com.dev6.rejordbe

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

import javax.persistence.EntityManager

/**
 * Querydsl test util
 */
@TestConfiguration
class TestConfig {
    @Bean
    JPAQueryFactory queryFactory(EntityManager em) {
        return new JPAQueryFactory(em)
    }
}

package com.dev6.rejordbe.application.id;

import org.springframework.lang.NonNull;

/**
 * IdGenerator
 * <p>테이블 고유 ID 생성</p>
 */
public interface IdGenerator {
    /**
     * 무작위 문자열을 이용한 아이디 생성
     *
     * @param prefix 무작위 문자열 앞에 위치할 prefix
     * @return {@code String} 무작위 문자열 ID 값
     */
    String generate(@NonNull String prefix);
}

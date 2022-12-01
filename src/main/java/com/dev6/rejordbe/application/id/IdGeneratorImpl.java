package com.dev6.rejordbe.application.id;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

/**
 * IdGeneratorImpl
 * <p>ID 생성 구현 클래스</p>
 */
final class IdGeneratorImpl implements IdGenerator {
    @Override
    public String generate(@NonNull final String prefix) {
        if (StringUtils.isBlank(prefix)) {
            throw new IllegalArgumentException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("-");
        stringBuilder.append(RandomStringUtils.random(10, true, true));
        return stringBuilder.toString();
    }
}

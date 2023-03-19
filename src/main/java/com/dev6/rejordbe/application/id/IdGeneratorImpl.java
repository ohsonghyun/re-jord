package com.dev6.rejordbe.application.id;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

/**
 * IdGeneratorImpl
 * <p>ID 생성 구현 클래스</p>
 */
@Slf4j
public final class IdGeneratorImpl implements IdGenerator {
    /**
     * {@inheritDoc}
     */
    @Override
    public String generate(@NonNull final String prefix) {
        if (StringUtils.isBlank(prefix)) {
            log.error("IdGeneratorImpl.generate: INTERNAL_ILLEGAL_PARAM: prefix: {}", prefix);
            throw new IllegalArgumentException(ExceptionCode.INTERNAL_ILLEGAL_PARAM.name());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefix);
        stringBuilder.append("-");
        stringBuilder.append(RandomStringUtils.random(10, true, true));
        return stringBuilder.toString();
    }
}

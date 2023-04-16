package com.dev6.rejordbe.application.badge.read;

import com.dev6.rejordbe.domain.badge.dto.BadgeByUidResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.infrastructure.badge.read.ReadBadgeRepository;
import org.springframework.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * BadgeInfoServiceImpl
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class BadgeInfoServiceImpl implements BadgeInfoService {

    private final ReadBadgeRepository readBadgeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BadgeByUidResult> findMyBadge(@NonNull final String uid) {
        if (StringUtils.isBlank(uid)) {
            log.warn("BadgeInfoServiceImpl.findMyBadge: ILLEGAL_UID: {}", uid);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_UID);
        }
        return readBadgeRepository.searchBadgeByUid(uid);
    }
}

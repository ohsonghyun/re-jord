package com.dev6.rejordbe.application.badge.read;

import com.dev6.rejordbe.domain.badge.BadgeCode;
import com.dev6.rejordbe.domain.badge.dto.BadgeByUidResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.infrastructure.badge.read.ReadBadgeRepository;
import org.springframework.lang.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public List<BadgeByUidResult> findBadgeByUid(@NonNull final String uid) {
        if (StringUtils.isBlank(uid)) {
            log.warn("BadgeInfoServiceImpl.findMyBadge: ILLEGAL_UID: {}", uid);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_UID);
        }

        List<BadgeByUidResult> list = readBadgeRepository.searchBadgeByUid(uid);
        // 받아온 list는 add가 되지 않아서 다시 addAll로 받아줘야 됨
        List<BadgeByUidResult> badgeList = new ArrayList<>();
        badgeList.addAll(list);
        BadgeCode[] code = BadgeCode.values();
        for(BadgeCode badge : code) {
            if(badgeList.stream().anyMatch(b -> badge.name().equals(b.getBadgeCode().name()))) {
                badgeList.set(badgeList.indexOf(new BadgeByUidResult(badge, null, null)), new BadgeByUidResult(badge, badge.getBadgeName(), badge.getImageUrl()));
            } else if(badge.equals(BadgeCode.DEFAULT)) {
                continue;
            } else {
                badgeList.add(new BadgeByUidResult(badge, badge.getBadgeName(), BadgeCode.DEFAULT.getImageUrl()));
            }
        }
        // badgeList 안에 객체를 정렬해주기 위해서 Comparator 인터페이스를 람다식으로 표현
        Collections.sort(badgeList, (badge1, badge2) -> badge1.getBadgeCode().name().compareTo(badge2.getBadgeCode().name()));
        return badgeList;
    }
}

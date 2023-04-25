package com.dev6.rejordbe.application.badge.read

import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.badge.dto.BadgeByUidResult
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.infrastructure.badge.read.ReadBadgeRepository
import spock.lang.Specification

/**
 * BadgeInfoServiceSpec
 */
class BadgeInfoServiceSpec extends Specification {

    private ReadBadgeRepository readBadgeRepository
    private BadgeInfoServiceImpl badgeInfoService

    def setup() {
        readBadgeRepository = Mock(ReadBadgeRepository)
        badgeInfoService = new BadgeInfoServiceImpl(readBadgeRepository)
    }

    def "uid가 일치하는 배지 정보를 획득할 수 있다"() {
        given:
        readBadgeRepository.searchBadgeByUid(_ as String)
                >> List.of(
                    new BadgeByUidResult(BadgeCode.WATER_FAIRY, null, null),
                    new BadgeByUidResult(BadgeCode.PRO_ACTIVATE, null, null),
                    new BadgeByUidResult(BadgeCode.MEAL_PLANNER, null, null),
                    new BadgeByUidResult(BadgeCode.PRO_SHOPPER, null, null),
                    new BadgeByUidResult(BadgeCode.ENERGY_SAVER, null, null)
        )

        when:
        def list = badgeInfoService.findBadgeByUid('uid')

        then:
        list.size() == 9
        list.get(0).getBadgeCode() == BadgeCode.DIGITAL_FAIRY
        list.get(0).getBadgeName() == BadgeCode.DIGITAL_FAIRY.getBadgeName()
        list.get(0).getImageUrl() == BadgeCode.DEFAULT.getImageUrl()
        list.get(1).getBadgeCode() == BadgeCode.ENERGY_SAVER
        list.get(1).getBadgeName() == BadgeCode.ENERGY_SAVER.getBadgeName()
        list.get(1).getImageUrl() == BadgeCode.ENERGY_SAVER.getImageUrl()
    }

    def "uid가 지정되지 않은 경우에는 에러: IllegalParameterException"() {
        when:
        badgeInfoService.findBadgeByUid(null)

        then:
        thrown(IllegalParameterException)
    }
}

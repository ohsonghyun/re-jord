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
                    new BadgeByUidResult(BadgeCode.WATER_FAIRY, 'imageUrl1'),
                    new BadgeByUidResult(BadgeCode.PRO_ACTIVATE, 'imageUrl2'),
                    new BadgeByUidResult(BadgeCode.MEAL_PLANNER, 'imageUrl3'),
                    new BadgeByUidResult(BadgeCode.PRO_SHOPPER, 'imageUrl4'),
                    new BadgeByUidResult(BadgeCode.ENERGY_SAVER, 'imageUrl5')
        )

        when:
        def list = badgeInfoService.findMyBadge('uid')

        then:
        list.size() == 5
        list.get(0).getBadgeCode() == BadgeCode.WATER_FAIRY
        list.get(0).getImageUrl() == 'imageUrl1'
    }

    def "uid가 지정되지 않은 경우에는 에러: IllegalParameterException"() {
        when:
        badgeInfoService.findMyBadge(null)

        then:
        thrown(IllegalParameterException)
    }
}

package com.dev6.rejordbe.application.footprint.read

import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.footprint.FootprintAcquirementType
import com.dev6.rejordbe.domain.footprint.dto.FootprintResult
import com.dev6.rejordbe.infrastructure.footprint.read.ReadFootPrintRepository
import org.assertj.core.api.Assertions
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * ReadFootprintServiceImplSpec
 */
class ReadFootprintServiceImplSpec extends Specification {

    ReadFootPrintRepository readFootPrintRepository
    ReadFootprintService readFootprintService

    def setup() {
        readFootPrintRepository = Mock(ReadFootPrintRepository)
        readFootprintService = new ReadFootprintServiceImpl(readFootPrintRepository)
    }

    def "UID로 내가 줄인 탄소 발자국을 취득할 수 있다"() {
        given:
        readFootPrintRepository.searchAllByUid(_ as String, _ as Pageable) >>
                new PageImpl<FootprintResult>(
                        List.of(
                                new FootprintResult('footprintId', 15, 'parentId', FootprintAcquirementType.CHALLENGE_REVIEW, 'title', BadgeCode.WATER_FAIRY, LocalDateTime.now())
                        ),
                        pageable,
                        2
                )

        when:
        def resultPage = readFootprintService.searchAllByUid('uid', pageable)

        then:
        resultPage.getTotalElements() == 1
        resultPage.getSize() == 5
        resultPage.getNumberOfElements() == 1
        def it = Assertions.assertThat(resultPage.getContent())
        it.extractingResultOf('getFootprintId').isNotNull()
        it.extractingResultOf('getFootprintAmount').containsExactly(15)
        it.extractingResultOf('getParentId').isNotNull()
        it.extractingResultOf('getFootprintAcquirementType').containsExactly(FootprintAcquirementType.CHALLENGE_REVIEW)
        it.extractingResultOf('getTitle').containsExactly('title')
        it.extractingResultOf('getBadgeCode').containsExactly(BadgeCode.WATER_FAIRY)
        it.extractingResultOf('getCreatedDate').isNotNull()

        where:
        pageable << [PageRequest.of(0, 5)]
    }

    // 실패케이스는 ReadFootPrintRepositoryTest.groovy 에 위임.

}

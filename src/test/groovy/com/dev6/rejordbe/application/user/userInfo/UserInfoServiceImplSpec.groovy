package com.dev6.rejordbe.application.user.userInfo


import com.dev6.rejordbe.application.user.userinfo.UserInfoService
import com.dev6.rejordbe.application.user.userinfo.UserInfoServiceImpl
import com.dev6.rejordbe.application.user.validate.UserInfoValidateService
import com.dev6.rejordbe.domain.role.Role
import com.dev6.rejordbe.domain.role.RoleType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage
import com.dev6.rejordbe.domain.user.dto.UserResult
import com.dev6.rejordbe.exception.DuplicatedNicknameException
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.MyPageInfoNotFoundException
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * UserInfoServiceImplSpec
 */
class UserInfoServiceImplSpec extends Specification {

    UserInfoRepository userInfoRepository
    UserInfoValidateService userInfoValidateService
    UserInfoService userInfoService

    def setup() {
        userInfoRepository = Mock(UserInfoRepository.class)
        userInfoValidateService = Mock(UserInfoValidateService.class)
        userInfoService = new UserInfoServiceImpl(userInfoRepository, userInfoValidateService)
    }

    def "UID로 유저 검색이 가능하다"() {
        given:
        def anUser = Users.builder()
                .uid(uid)
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .roles(Collections.singletonList(new Role(roleType)))
                .build()
        // mock
        userInfoRepository.findUserByUid(_ as String) >> Optional.of(anUser)

        expect:
        def userResult = userInfoService.findUserByUid(uid).orElseThrow()

        userResult != null
        userResult.getUid() == uid
        userResult.getUserId() == userId
        userResult.getNickname() == nickname
        userResult.getPassword() == null
        userResult.getRoles().size() == 1
        userResult.getRoles().get(0) == roleType

        where:
        uid   | userId   | nickname   | password   | roleType
        "uid" | "userId" | "nickname" | "password" | RoleType.ROLE_USER
    }

    def "유저의 닉네임이 변경 가능하다"() {
        given:
        def anUser = Users.builder()
                .nickname(nickname)
                .build()

        // mock
        userInfoRepository.findById(_) >> Optional.of(anUser)
        userInfoRepository.findUserByNickname(_ as String) >> Optional.empty()
        userInfoValidateService.validateNickname(_ as String, _ as List<RuntimeException>) >> true


        when:
        userInfoService.updateUserInfo(Users.builder().nickname(newNickname).build())

        then:
        anUser.getNickname() == newNickname

        where:
        nickname   | newNickname
        'nickname' | 'newNickname'
    }

    def "새로운 닉네임이 정책을 만족시키지 못하면 에러: IllegalParameterException"() {
        given:
        userInfoValidateService.validateNickname(_ as String, _ as List<RuntimeException>) >> false

        when:
        userInfoService.updateUserInfo(Users.builder().nickname("n").build())

        then:
        thrown(IllegalParameterException)
    }

    def "닉네임을 수정할 유저가 존재하지 않으면 에러: UserNotFoundException"() {
        given:
        // mock
        userInfoRepository.findById(_) >> Optional.empty()
        userInfoRepository.findUserByNickname(_ as String) >> Optional.empty()
        userInfoValidateService.validateNickname(_ as String, _ as List<RuntimeException>) >> true

        when:
        userInfoService.updateUserInfo(Users.builder().nickname("nickname").build())

        then:
        thrown(UserNotFoundException)
    }

    def "동일한 닉네임이 존재하면 에러: DuplicatedNicknameException"() {
        given:
        // mock
        userInfoRepository.findUserByNickname(_ as String) >> Optional.of(Users.builder().build())
        userInfoValidateService.validateNickname(_ as String, _ as List<RuntimeException>) >> true

        when:
        userInfoService.updateUserInfo(Users.builder().nickname("nickname").build())

        then:
        thrown(DuplicatedNicknameException)
    }

    // ----------------------------------------------------
    // 마이페이지 관련
    // ----------------------------------------------------

    def "에러가 없으면 마이페이지 정보를 취득할 수 있다."() {
        def anUser = Users.builder()
                .uid('uid')
                .userId('userId')
                .nickname(nickname)
                .password('password')
                .roles(Collections.singletonList(new Role(RoleType.ROLE_USER)))
                .build()
        userInfoRepository.findById(_ as String) >> Optional.of(anUser)
        userInfoRepository.searchUserInfoByUid(_ as String) >> Optional.of(UserInfoForMyPage.builder()
                .badgeAmount(badgeAmount)
                .totalFootprintAmount(totalFootprintAmount)
                .nickname(nickname)
                .createdDate(createdDate)
                .build())

        when:
        def result = userInfoService.findUserInfoByUid(uid)

        then:
        result.getNickname() == nickname
        result.getTotalFootprintAmount() == totalFootprintAmount
        result.getBadgeAmount() == badgeAmount
        result.getDDay() == ChronoUnit.DAYS.between(createdDate, LocalDateTime.now())

        where:
        nickname   | badgeAmount | totalFootprintAmount | uid   | createdDate
        'nickname' | 3           | 45                   | 'uid' | LocalDateTime.of(2022, 3, 15, 9, 30, 30)
    }

    def "마이페이지 정보를 검색할 유저가 존재하지 않으면 에러: UserNotFoundException"() {
        given:
        // mock
        userInfoRepository.findById(_ as String) >> Optional.empty()

        when:
        userInfoService.findUserInfoByUid('uid')

        then:
        thrown(UserNotFoundException)
    }

    def "마이페이지 정보가 존재하지 않으면 에러: MyPageInfoNotFoundException"() {
        given:
        def anUser = Users.builder()
                .uid('uid')
                .build()

        // mock
        userInfoRepository.findById(_ as String) >> Optional.of(anUser)
        userInfoRepository.searchUserInfoByUid(_ as String) >> Optional.empty()

        when:
        userInfoService.findUserInfoByUid('uid')

        then:
        thrown(MyPageInfoNotFoundException)
    }
}

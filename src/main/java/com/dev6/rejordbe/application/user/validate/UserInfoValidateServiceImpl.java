package com.dev6.rejordbe.application.user.validate;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.role.RoleType;
import com.dev6.rejordbe.exception.IllegalParameterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * UserInfoValidateServiceImpl
 */
@Component
@Slf4j
public class UserInfoValidateServiceImpl implements UserInfoValidateService {
    /**
     * {@inheritDoc}
     * <ol>
     *     <li>영문(소문자), 숫자 조합으로만 입력 가능합니다.</li>
     *     <li>영문으로만 생성가능하나, 숫자로만 생성은 불가능</li>
     *     <li>영문(소문자)은 최소 5자 이상 ~ 최대 20자 이하</li>
     *     <li>대문자 및 특수문자 사용 금지</li>
     * </ol>
     */
    @Override
    public boolean validateUserId(String userId, List<RuntimeException> errors) {
        boolean errorFree = true;
        if (StringUtils.isBlank(userId)) {
            log.info("UserInfoValidateServiceImpl.validateUserId: UserId is empty or null: {}", userId);
            errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_USERID));
            errorFree = false;
        } else {
            // 영문, 숫자 조합으로 5~20자 이내
            if (userId.length() < 5 || userId.length() > 20) {
                log.info("UserInfoValidateServiceImpl.validateUserId: Wrong userId's length: {}", userId);
                errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_USERID));
                errorFree = false;
            }

            // 영문(소문자), 숫자 이외의 문자가 있는면 에러
            // 아이디가 숫자만으로 구성되어 있으면 에러
            final Pattern pattern = Pattern.compile("(^\\d+$|\\W|[A-Z])");
            if (pattern.matcher(userId).find()) {
                log.info("UserInfoValidateServiceImpl.validateUserId: Violation against UserId rule: {}", userId);
                errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_USERID));
                return false;
            }
        }
        return errorFree;
    }

    /**
     * {@inheritDoc}
     * <ol>
     *     <li>영문, 한글, 숫자 구성 가능</li>
     *     <li>최소 2글자 이상 ~ 최대 10글자 이하</li>
     * </ol>
     */
    @Override
    public boolean validateNickname(String nickname, List<RuntimeException> errors) {
        boolean errorFree = true;
        if (StringUtils.isBlank(nickname)) {
            log.info("UserInfoValidateServiceImpl.validateNickname: Nickname is empty or null: {}", nickname);
            errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_NICKNAME));
            errorFree = false;
        } else {
            // 한글, 영문, 숫자, 특수문자 조합으로 2~10자 이내
            if (nickname.length() < 2 || nickname.length() > 10) {
                log.info("UserInfoValidateServiceImpl.validateNickname: Wrong nickname's length: {}", nickname);
                errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_NICKNAME));
                errorFree = false;
            }

            // 영문, 한글, 숫자이외의 문자가 포함되어 있으면 에러
            Pattern pattern = Pattern.compile("[^\\w|가-힣]");
            if (pattern.matcher(nickname).find()) {
                log.info("UserInfoValidateServiceImpl.validateNickname: Violation against Nickname rule: {}", nickname);
                errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_NICKNAME));
                errorFree = false;
            }
        }
        return errorFree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validatePassword(String password, List<RuntimeException> errors) {
        boolean errorFree = true;
        if (StringUtils.isBlank(password)) {
            log.info("UserInfoValidateServiceImpl.validatePassword: Password is empty: {}", password);
            errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_PASSWORD));
            errorFree = false;
        }
        return errorFree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateRoleTypes(List<String> roleNames, List<RuntimeException> errors) {
        if (CollectionUtils.isEmpty(roleNames)) {
            log.info("UserInfoValidateServiceImpl.validateRoleTypes: roleNames is empty: {}", roleNames);
            errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_ROLE));
            return false;
        }
        List<String> roleTypes = List.of(RoleType.ROLE_ADMIN, RoleType.ROLE_USER);
        for (String roleName : roleNames) {
            if (!roleTypes.contains(roleName)) {
                log.info("UserInfoValidateServiceImpl.validateRoleTypes: Unknown roleNames: {}", roleNames);
                errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_ROLE));
                return false;
            }
        }
        return true;
    }
}

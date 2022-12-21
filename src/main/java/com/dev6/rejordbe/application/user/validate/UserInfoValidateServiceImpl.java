package com.dev6.rejordbe.application.user.validate;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.IllegalParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * UserInfoValidateServiceImpl
 */
@Component
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
            errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_USERID.name()));
            errorFree = false;
        } else {
            // 영문, 숫자 조합으로 5~20자 이내
            if (userId.length() < 5 || userId.length() > 20) {
                errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_USERID.name()));
                errorFree = false;
            }

            // 영문(소문자), 숫자 이외의 문자가 있는면 에러
            // 아이디가 숫자만으로 구성되어 있으면 에러
            final Pattern pattern = Pattern.compile("(^\\d+$|\\W|[A-Z])");
            if (pattern.matcher(userId).find()) {
                errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_USERID.name()));
                return false;
            }
        }
        return errorFree;
    }

    /**
     * {@inheritDoc}
     * <ol>
     *     <li>영문, 한글, 숫자 구성 가능</li>
     *     <li>최소 2글자 이상 ~ 최대 15글자 이하</li>
     * </ol>
     */
    @Override
    public boolean validateNickname(String nickname, List<RuntimeException> errors) {
        boolean errorFree = true;
        if (StringUtils.isBlank(nickname)) {
            errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_NICKNAME.name()));
            errorFree = false;
        } else {
            // 한글, 영문, 숫자, 특수문자 조합으로 2~15자 이내
            if (nickname.length() < 2 || nickname.length() > 15) {
                errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_NICKNAME.name()));
                errorFree = false;
            }

            // 영문, 한글, 숫자이외의 문자가 포함되어 있으면 에러
            Pattern pattern = Pattern.compile("[^\\w|가-힣]");
            if (pattern.matcher(nickname).find()) {
                errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_NICKNAME.name()));
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
            errors.add(new IllegalParameterException(ExceptionCode.ILLEGAL_PASSWORD.name()));
            errorFree = false;
        }
        return errorFree;
    }
}

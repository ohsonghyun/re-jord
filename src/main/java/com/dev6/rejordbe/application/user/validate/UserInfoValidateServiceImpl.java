package com.dev6.rejordbe.application.user.validate;

import com.dev6.rejordbe.exception.IllegalParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * UserInfoValidateServiceImpl
 */
@Component
public class UserInfoValidateServiceImpl implements UserInfoValidateService {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateUserId(String userId, List<RuntimeException> errors) {
        boolean errorFree = true;
        if (StringUtils.isBlank(userId)) {
            errors.add(new IllegalParameterException("ILLEGAL_USERID"));
            errorFree = false;
        } else {
            // 영문, 숫자 조합으로 5~20자 이내
            if (userId.length() < 5 || userId.length() > 20) {
                errors.add(new IllegalParameterException("ILLEGAL_USERID"));
                errorFree = false;
            }
            // TODO 영문, 숫자 이외의 문자가 있는지 체크 필요
        }
        return errorFree;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateNickname(String nickname, List<RuntimeException> errors) {
        boolean errorFree = true;
        if (StringUtils.isBlank(nickname)) {
            errors.add(new IllegalParameterException("ILLEGAL_NICKNAME"));
            errorFree = false;
        } else {
            // 한글, 영문, 숫자, 특수문자 조합으로 2~15자 이내
            if (nickname.length() < 2 || nickname.length() > 15) {
                errors.add(new IllegalParameterException("ILLEGAL_NICKNAME"));
                errorFree = false;
            }
            // TODO 한글, 영문, 숫자, 툭수문자 이외의 문자가 있는지 체크 필요
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
            errors.add(new IllegalParameterException("ILLEGAL_PASSWORD"));
            errorFree = false;
        }
        return errorFree;
    }
}

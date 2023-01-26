package com.dev6.rejordbe.domain.exception;

import org.eclipse.jdt.internal.compiler.env.IModule;

/**
 * ExceptionCode
 */
public enum ExceptionCode {
    ILLEGAL_UID,
    ILLEGAL_USERID,
    ILLEGAL_NICKNAME,
    ILLEGAL_PASSWORD,
    ILLEGAL_ROLE,
    ILLEGAL_DATE_TIME,
    ILLEGAL_CONTENTS,
    ILLEGAL_ACCESS,
    DUPLICATED_NICKNAME,
    DUPLICATED_USERID,
    USER_NOT_FOUND,
    CHALLENGE_REVIEW_NOT_FOUND,

    // INTERNAL_SERVER_ERROR -------------------------------------
    INTERNAL_ILLEGAL_PARAM;
}

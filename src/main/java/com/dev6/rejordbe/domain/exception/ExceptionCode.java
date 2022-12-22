package com.dev6.rejordbe.domain.exception;

/**
 * ExceptionCode
 */
public enum ExceptionCode {
    ILLEGAL_USERID,
    ILLEGAL_NICKNAME,
    ILLEGAL_PASSWORD,
    DUPLICATED_NICKNAME,
    DUPLICATED_USERID,
    USER_NOT_FOUND,

    // INTERNAL_SERVER_ERROR -------------------------------------
    INTERNAL_ILLEGAL_PARAM
}

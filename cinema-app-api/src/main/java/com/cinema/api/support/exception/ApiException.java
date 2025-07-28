package com.cinema.api.support.exception;

public class ApiException extends RuntimeException {
    private final ApiErrorCode errorCode;

    public ApiException(ApiErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ApiErrorCode getErrorCode() {
        return errorCode;
    }
}

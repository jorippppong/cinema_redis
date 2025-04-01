package com.cinema.core.support.exception;

public class CoreException extends RuntimeException {
    private final CoreErrorCode errorCode;

    public CoreException(CoreErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CoreErrorCode getErrorCode() {
        return errorCode;
    }
}

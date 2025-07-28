package com.cinema.api.support.exception;

public record ValidationError(
        String field,
        String message
) {
}

package com.cinema.api.support;

public record ValidationError(
	String field,
	String message
) {
}

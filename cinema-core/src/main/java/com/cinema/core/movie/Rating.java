package com.cinema.core.movie;

public enum Rating {
	ALL("전체 관람가"),
	PG_12("12세 이상 관람가"),
	PG_15("15세 이상 관람가"),
	NC_19("청소년 관람 불가"),
	RESTRICT("제한관람가");

	private final String description;

	Rating(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}

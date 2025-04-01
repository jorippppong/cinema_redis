package com.cinema.core.domains.screen;

import java.util.Set;

import org.springframework.stereotype.Repository;

import com.cinema.core.domains.ticketing.Seat;

@Repository
public interface SeatRepository {
	Set<Seat> getByScreenIdAndSeatNumbers(Long aLong, Set<String> seats);
}

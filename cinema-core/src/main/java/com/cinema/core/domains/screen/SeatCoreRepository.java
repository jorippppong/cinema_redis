package com.cinema.core.domains.screen;

import com.cinema.core.domains.ticketing.Seat;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class SeatCoreRepository implements SeatRepository {
    private final SeatJpaRepository seatJpaRepository;

    public SeatCoreRepository(SeatJpaRepository seatJpaRepository) {
        this.seatJpaRepository = seatJpaRepository;
    }

    @Override
    public Set<Seat> getByScreenIdAndSeatNumbers(Long screenId, Set<String> seatNumbers) {
        Set<SeatEntity> seatEntities = seatJpaRepository.getByScreenIdAndSeatNumbers(screenId, seatNumbers);
        return seatEntities.stream()
                .map(SeatEntity::toSeat)
                .collect(Collectors.toSet());
    }
}

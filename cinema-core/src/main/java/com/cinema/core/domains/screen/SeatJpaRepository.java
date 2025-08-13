package com.cinema.core.domains.screen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
    @Query("select s from SeatEntity s where s.screen.id = :screenId and s.seatNumber in :seatNumbers")
    Set<SeatEntity> getByScreenIdAndSeatNumbers(@Param("screenId") Long screenId,
                                                @Param("seatNumbers") Set<String> seatNumbers);
}

package com.cinema.core.domains.schedule;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinema.core.domains.movie.Genre;
import com.cinema.core.support.CoreErrorCode;
import com.cinema.core.support.CoreException;

@Service
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;

	public ScheduleService(ScheduleRepository scheduleRepository) {
		this.scheduleRepository = scheduleRepository;
	}

	// 상영중인 영화 조회
	public List<Schedule> getOngoingSchedule(String title, Genre genre) {
		return scheduleRepository.getSchedule(title, genre);
	}

	public Schedule findById(Long id) {
		return scheduleRepository.findById(id)
			.orElseThrow(() -> new CoreException(CoreErrorCode.SCHEDULE_NOT_FOUND));
	}
}

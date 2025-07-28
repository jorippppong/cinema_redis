package com.cinema.api.v1.ticketing;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinema.api.v1.ticketing.dto.CreateTicketingRequest;
import com.cinema.core.domains.ticketing.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/ticketing")
public class TicketingController {
	private final TicketService ticketingService;

	public TicketingController(TicketService ticketingService) {
		this.ticketingService = ticketingService;
	}

	@PostMapping()
	public ResponseEntity<Void> createTicketing(
		@Valid @RequestBody CreateTicketingRequest request
	) {
		ticketingService.createTicketing(request.toCreateTicketingCommand());
		// TODO : FCM 알림 전송 로직 추가
		return ResponseEntity.ok()
			.build();
	}
}

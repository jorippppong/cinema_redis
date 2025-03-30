package com.cinema.core.domains.user;

import static com.cinema.core.support.CoreErrorCode.*;

import org.springframework.stereotype.Service;

import com.cinema.core.support.CoreException;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User findById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new CoreException(USER_NOT_FOUND));
	}
}

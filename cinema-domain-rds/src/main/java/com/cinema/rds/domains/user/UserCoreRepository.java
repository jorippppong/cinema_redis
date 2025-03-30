package com.cinema.rds.domains.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.cinema.core.domains.user.User;
import com.cinema.core.domains.user.UserRepository;

@Repository
public class UserCoreRepository implements UserRepository {
	private final UserJpaRepository userJpaRepository;

	public UserCoreRepository(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}

	@Override
	public Optional<User> findById(Long id) {
		return userJpaRepository.findById(id)
			.map(UserEntity::toUser);
	}
}

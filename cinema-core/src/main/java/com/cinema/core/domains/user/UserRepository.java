package com.cinema.core.domains.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
	Optional<User> findById(Long id);
}

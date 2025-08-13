package com.cinema.core.domains.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;

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

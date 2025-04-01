package com.cinema.core.domains.user;

import com.cinema.core.support.exception.CoreException;
import org.springframework.stereotype.Service;

import static com.cinema.core.support.exception.CoreErrorCode.USER_NOT_FOUND;

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

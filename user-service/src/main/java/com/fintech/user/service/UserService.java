package com.fintech.user.service;

import com.fintech.user.model.User;
import com.fintech.user.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * condition: không cache khi userId null/blank (tránh key rác).
     * unless: không đưa kết quả null vào cache.
     */
    @Cacheable(
            value = "users",
            key = "#userId",
            condition = "#userId != null and !#userId.isBlank()",
            unless = "#result == null"
    )
    public User getUserById(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId must not be null or blank");
        }
        System.out.println(">>> Truy vấn Database cho userId: " + userId);
        return userRepository.findById(userId).orElse(null);
    }
}

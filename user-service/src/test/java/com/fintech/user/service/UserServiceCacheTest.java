package com.fintech.user.service;

import com.fintech.user.model.User;
import com.fintech.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceCacheTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void secondCall_shouldHitCache_andNotQueryDbAgain() {
        User user = new User("u1", "Nguyen Van A", "a@bank.vn");
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        // Cache miss — gọi DB
        User first = userService.getUserById("u1");
        // Cache hit — không gọi DB lại
        User second = userService.getUserById("u1");

        assertEquals("Nguyen Van A", first.getFullName());
        assertEquals(first.getFullName(), second.getFullName());

        verify(userRepository, times(1)).findById("u1");
    }
}

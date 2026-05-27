package com.swissroute.swissroute.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        // 🔥 inject expiration manually (since we are NOT using Spring context)
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600L);

        jwtUtil.init(); // generate key

        userDetails = new User(
            "juan@example.com",
            "password",
            new ArrayList<>()
        );
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String token = jwtUtil.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken(userDetails);

        String username = jwtUtil.extractUsername(token);

        assertEquals("juan@example.com", username);
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken(userDetails);

        Boolean isValid = jwtUtil.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void extractExpiration_ShouldBeInFuture() {
        String token = jwtUtil.generateToken(userDetails);

        assertTrue(
            jwtUtil.extractExpiration(token).after(new java.util.Date())
        );
    }
}

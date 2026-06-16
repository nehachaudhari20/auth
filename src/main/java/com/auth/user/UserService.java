package com.auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(SignupRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .email(request.email())
                .passwordHash(
                        passwordEncoder.encode(request.password()))
                .role(UserRole.USER)
                .createdAt(Instant.now())
                .build();

        userRepository.save(user);
    }
}
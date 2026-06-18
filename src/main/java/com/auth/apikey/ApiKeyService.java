package com.auth.apikey;

import com.auth.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository repository;
    private final PasswordEncoder passwordEncoder;

    public String createKey(
            User user,
            String name) {

        String rawKey = "sk_live_" +
                UUID.randomUUID();

        ApiKey apiKey = ApiKey.builder()
                .id(UUID.randomUUID())
                .user(user)
                .name(name)
                .keyHash(
                        passwordEncoder.encode(rawKey))
                .createdAt(Instant.now())
                .build();

        repository.save(apiKey);

        return rawKey;
    }
}
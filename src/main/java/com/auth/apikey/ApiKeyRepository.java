package com.auth.apikey;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApiKeyRepository
        extends JpaRepository<ApiKey, UUID> {
}
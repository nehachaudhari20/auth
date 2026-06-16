package com.auth.auth;

public record LoginRequest(
        String email,
        String password) {
}
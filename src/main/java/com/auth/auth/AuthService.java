package com.auth.auth;

import com.auth.audit.AuditService;
import com.auth.security.JwtService;
import com.auth.user.Session;
import com.auth.user.SessionRepository;
import com.auth.user.SignupRequest;
import com.auth.user.User;
import com.auth.user.UserRepository;
import com.auth.user.UserService;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SessionRepository sessionRepository;
    private final AuditService auditService;

    public void signup(SignupRequest request) {
        userService.createUser(request);

        auditService.log(
                request.email(),
                "USER_REGISTERED",
                "New account created");
    }

    private RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = RefreshToken.builder()
                .id(UUID.randomUUID())
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    auditService.log(
                            request.email(),
                            "LOGIN_FAILED",
                            "Invalid credentials");

                    return new RuntimeException("Invalid credentials");
                });

        boolean matches = passwordEncoder.matches(
                request.password(),
                user.getPasswordHash());

        if (!matches) {
            auditService.log(
                    request.email(),
                    "LOGIN_FAILED",
                    "Invalid credentials");
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name());

        RefreshToken refreshToken = createRefreshToken(user);
        createSession(user, refreshToken);

        auditService.log(
                user.getEmail(),
                "LOGIN_SUCCESS",
                "User authenticated");

        return new AuthResponse(
                accessToken,
                refreshToken.getToken());
    }

    public AuthResponse refreshToken(
            RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(
                        request.refreshToken())
                .orElseThrow(() -> new RuntimeException(
                        "Invalid refresh token"));

        if (refreshToken.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new RuntimeException(
                    "Refresh token expired");
        }

        String accessToken = jwtService.generateToken(
                refreshToken.getUser().getEmail(),
                refreshToken.getUser().getRole().name());

        auditService.log(
                refreshToken.getUser().getEmail(),
                "TOKEN_REFRESH",
                "Access token refreshed");

        return new AuthResponse(
                accessToken,
                refreshToken.getToken());
    }

    private void createSession(
            User user,
            RefreshToken refreshToken) {

        Session session = Session.builder()
                .id(UUID.randomUUID())
                .user(user)
                .refreshToken(refreshToken)
                .deviceName("Postman")
                .ipAddress("127.0.0.1")
                .createdAt(Instant.now())
                .lastActiveAt(Instant.now())
                .build();

        sessionRepository.save(session);

        auditService.log(
                user.getEmail(),
                "SESSION_CREATED",
                "New session created");
    }
}

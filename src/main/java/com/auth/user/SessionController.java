package com.auth.user;

import com.auth.audit.AuditService;
import com.auth.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuditService auditService;

    @GetMapping
    public List<Session> getSessions(
            Authentication authentication) {

        User user = userRepository
                .findByEmail(
                        authentication.getName())
                .orElseThrow();

        return sessionRepository.findByUserId(
                user.getId());
    }

    @DeleteMapping("/{sessionId}")
    public void deleteSession(
            @PathVariable UUID sessionId,
            Authentication authentication) {

        Session session = sessionRepository
                .findById(sessionId)
                .orElseThrow();

        refreshTokenRepository.delete(
                session.getRefreshToken());

        sessionRepository.delete(session);

        auditService.log(
                authentication.getName(),
                "SESSION_REVOKED",
                "Session deleted");
    }

    @DeleteMapping("/all")
    public void deleteAllSessions(
            Authentication authentication) {

        User user = userRepository
                .findByEmail(
                        authentication.getName())
                .orElseThrow();

        List<Session> sessions = sessionRepository.findByUser(user);

        for (Session session : sessions) {

            refreshTokenRepository.delete(
                    session.getRefreshToken());

            sessionRepository.delete(session);
        }

        auditService.log(
                authentication.getName(),
                "LOGOUT_ALL",
                "All sessions revoked");
    }
}

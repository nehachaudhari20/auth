package com.auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

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
}
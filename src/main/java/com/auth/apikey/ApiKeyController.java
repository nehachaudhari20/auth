package com.auth.apikey;

import com.auth.user.User;
import com.auth.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apikeys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;
    private final UserRepository userRepository;

    @PostMapping
    public String createKey(
            Authentication authentication,
            @RequestBody CreateApiKeyRequest request) {

        User user = userRepository
                .findByEmail(
                        authentication.getName())
                .orElseThrow();

        return apiKeyService.createKey(
                user,
                request.name());
    }
}
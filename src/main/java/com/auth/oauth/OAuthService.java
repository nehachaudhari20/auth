package com.auth.oauth;

import com.auth.security.JwtService;
import com.auth.user.User;
import com.auth.user.UserRepository;
import com.auth.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final OAuthAccountRepository oauthAccountRepository;
    private final JwtService jwtService;

    public String handleGoogleLogin(
            String providerUserId,
            String email) {

        OAuthAccount account = oauthAccountRepository
                .findByProviderAndProviderUserId(
                        "GOOGLE",
                        providerUserId)
                .orElse(null);

        if (account != null) {

            return jwtService.generateToken(
                    account.getUser().getEmail(),
                    account.getUser().getRole().name());
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {

                    User newUser = User.builder()
                            .id(UUID.randomUUID())
                            .email(email)
                            .passwordHash("OAUTH_USER")
                            .role(UserRole.USER)
                            .createdAt(Instant.now())
                            .build();

                    return userRepository.save(newUser);
                });

        OAuthAccount oauthAccount = OAuthAccount.builder()
                .id(UUID.randomUUID())
                .user(user)
                .provider("GOOGLE")
                .providerUserId(providerUserId)
                .createdAt(Instant.now())
                .build();

        oauthAccountRepository.save(oauthAccount);

        return jwtService.generateToken(
                user.getEmail(),
                user.getRole().name());
    }
}
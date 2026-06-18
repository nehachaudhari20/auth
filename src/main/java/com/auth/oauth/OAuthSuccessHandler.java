package com.auth.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler
        implements AuthenticationSuccessHandler {

    private final OAuthService oauthService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

        String googleId = oauthUser.getAttribute("sub");

        String email = oauthUser.getAttribute("email");

        String jwt = oauthService.handleGoogleLogin(
                googleId,
                email);

        response.setContentType(
                "application/json");

        response.getWriter().write(
                """
                        {
                          "accessToken":"%s"
                        }
                        """.formatted(jwt));
    }
}
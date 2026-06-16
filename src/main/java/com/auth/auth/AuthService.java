package com.auth.auth;

import com.auth.security.JwtService;
import com.auth.user.SignupRequest;
import com.auth.user.User;
import com.auth.user.UserRepository;
import com.auth.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void signup(SignupRequest request) {
        userService.createUser(request);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        boolean matches = passwordEncoder.matches(
                request.password(),
                user.getPasswordHash());

        if (!matches) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}
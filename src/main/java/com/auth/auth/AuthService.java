package com.auth.auth;

import com.auth.user.SignupRequest;
import com.auth.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    public void signup(SignupRequest request) {
        userService.createUser(request);
    }
}
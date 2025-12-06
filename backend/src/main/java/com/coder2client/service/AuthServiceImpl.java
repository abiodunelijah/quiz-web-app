package com.coder2client.service;

import com.coder2client.dtos.AuthResponse;
import com.coder2client.dtos.LoginRequest;
import com.coder2client.dtos.RegisterRequest;
import com.coder2client.entities.User;
import com.coder2client.repositories.UserRepository;
import com.coder2client.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return AuthResponse.builder().message("Username already exists").build();
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return AuthResponse.builder().message("Email already exists").build();
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .message("User registered successfully")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            String token = jwtService.generateToken(user.getUsername());

            return AuthResponse.builder()
                    .token(token)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .message("Login successful")
                    .build();

        } catch (Exception ex) {
            return AuthResponse.builder().message("Invalid username or password").build();
        }
    }
}


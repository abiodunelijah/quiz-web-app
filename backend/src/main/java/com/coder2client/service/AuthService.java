package com.coder2client.service;

import com.coder2client.dtos.AuthResponse;
import com.coder2client.dtos.LoginRequest;
import com.coder2client.dtos.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}


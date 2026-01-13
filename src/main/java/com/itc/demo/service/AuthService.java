package com.itc.demo.service;

import com.itc.demo.dto.request.LoginRequestDTO;
import com.itc.demo.dto.response.LoginResponseDTO;
import com.itc.demo.dto.response.UserResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO dto);
    UserResponseDTO getCurrentUser(String email);
}

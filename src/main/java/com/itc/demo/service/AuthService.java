package com.itc.demo.service;

import com.itc.demo.dto.request.LoginRequestDTO;
import com.itc.demo.dto.response.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO dto);
}

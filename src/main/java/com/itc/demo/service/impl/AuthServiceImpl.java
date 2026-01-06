package com.itc.demo.service.impl;

import com.itc.demo.dto.request.LoginRequestDTO;
import com.itc.demo.dto.response.LoginResponseDTO;
import com.itc.demo.dto.response.UserResponseDTO;
import com.itc.demo.entity.User;
import com.itc.demo.repository.UserRepository;
import com.itc.demo.service.AuthService;
import com.itc.demo.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository repository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = repository.findByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("Invalid credentials"));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user);
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());

        LoginResponseDTO.Data  data= new LoginResponseDTO.Data();
        data.setToken(token);
        data.setUser(dto);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setSuccess(true);
        response.setData(data);

        return response;
    }
}

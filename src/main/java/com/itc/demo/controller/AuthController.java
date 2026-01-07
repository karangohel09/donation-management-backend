package com.itc.demo.controller;

import com.itc.demo.dto.request.LoginRequestDTO;
import com.itc.demo.dto.response.LoginResponseDTO;
import com.itc.demo.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request){
            return ResponseEntity.ok(authService.login(request));
    }
}

package com.itc.demo.service;

import com.itc.demo.dto.request.CreateUserRequest;
import com.itc.demo.dto.response.UserResponseDTO;
import com.itc.demo.enums.Role;

import java.util.List;

public interface UserService {
    UserResponseDTO createUser(CreateUserRequest request);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUserRole(Long userId, Role role);
}

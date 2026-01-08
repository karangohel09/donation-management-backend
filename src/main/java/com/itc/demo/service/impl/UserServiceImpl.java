package com.itc.demo.service.impl;

import com.itc.demo.dto.request.CreateUserRequest;
import com.itc.demo.dto.response.UserResponseDTO;
import com.itc.demo.entity.User;
import com.itc.demo.enums.Role;
import com.itc.demo.mapper.UserMapper;
import com.itc.demo.repository.UserRepository;
import com.itc.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder, UserMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDTO createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = repository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return repository
                .findAll()
                .stream().
                map(userMapper::toDTO).
                collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUserRole(Long userId, Role role) {
        User user = repository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
                repository.save(user);

        return userMapper.toDTO(user);
    }
}

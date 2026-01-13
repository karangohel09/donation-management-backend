package com.itc.demo.service.impl;

import com.itc.demo.dto.request.CreateUserRequest;
import com.itc.demo.dto.request.UpdateUserRequest;
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
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repository,
                           UserMapper userMapper,
                           PasswordEncoder encoder) {
        this.repository = repository;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    @Override
    public UserResponseDTO createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = repository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUserRole(Long userId, Role role) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(role);
        User updated = repository.save(user);
        return userMapper.toDTO(updated);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UpdateUserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setActive(request.isActive());

        User updated = repository.save(user);
        return userMapper.toDTO(updated);
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void resetPassword(Long id, String newPassword) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(encoder.encode(newPassword));
        repository.save(user);
    }
}

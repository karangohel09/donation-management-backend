package com.itc.demo.controller;

import com.itc.demo.dto.request.CreateUserRequest;
import com.itc.demo.dto.request.ResetPasswordRequest;
import com.itc.demo.dto.request.UpdateUserRequest;
import com.itc.demo.dto.response.UserResponseDTO;
import com.itc.demo.enums.Role;
import com.itc.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final UserService userService;

    public SettingsController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/users")
    public List<UserResponseDTO> getUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/users")
    public UserResponseDTO createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/users/{id}")
    public UserResponseDTO updateUser(@PathVariable Long id,
                                      @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/users/{id}/password")
    public void resetPassword(@PathVariable Long id,
                              @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(id, request.getNewPassword());
    }

    @GetMapping("/roles")
    public List<String> getRoles() {
        return Arrays.stream(Role.values()).map(Enum::name).toList();
    }
}

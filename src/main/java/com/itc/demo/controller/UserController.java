package com.itc.demo.controller;

import com.itc.demo.dto.request.CreateUserRequest;
import com.itc.demo.dto.request.UpdateRoleRequest;
import com.itc.demo.dto.response.UserResponseDTO;
import com.itc.demo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public UserResponseDTO createNewUser(@RequestBody CreateUserRequest request){
        return service.createUser(request);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping
    public List<UserResponseDTO> getAllUser(){
        return service.getAllUsers();
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}/role")
    public UserResponseDTO updateUserRole(@PathVariable("id") Long userId,
                                          @RequestBody UpdateRoleRequest request) {
        return service.updateUserRole(userId, request.getRole());
    }

}

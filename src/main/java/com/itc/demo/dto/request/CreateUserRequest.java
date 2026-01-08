package com.itc.demo.dto.request;

import com.itc.demo.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}

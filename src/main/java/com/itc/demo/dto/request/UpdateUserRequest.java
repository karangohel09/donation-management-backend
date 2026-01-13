package com.itc.demo.dto.request;

import com.itc.demo.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String name;
    private String email;
    private Role role;
    private boolean active;
}

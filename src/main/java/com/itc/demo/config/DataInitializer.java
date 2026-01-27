package com.itc.demo.config;

import com.itc.demo.entity.User;
import com.itc.demo.enums.Role;
import com.itc.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initSuperAdmin() {

        if (userRepository.count() == 0) {

            User superAdmin = new User();
            superAdmin.setName("Super Admin");
            superAdmin.setEmail("admin@itc.com");
            superAdmin.setPassword(passwordEncoder.encode("admin123"));
            superAdmin.setRole(Role.SUPER_ADMIN);

            userRepository.save(superAdmin);

            System.out.println("SUPER_ADMIN created successfully");
        }
    }
}

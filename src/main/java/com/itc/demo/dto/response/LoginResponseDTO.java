package com.itc.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private Boolean success;
    private Data data;

    public static class Data{
        private String token;
        private UserResponseDTO user;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserResponseDTO getUser() {
            return user;
        }

        public void setUser(UserResponseDTO user) {
            this.user = user;
        }
    }
}

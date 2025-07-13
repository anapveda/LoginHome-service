package com.players.LoginHome_service.dto;

import com.players.LoginHome_service.model.enums.UserRole;
import lombok.Data;

import java.nio.file.FileStore;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
    private String userRole;


}

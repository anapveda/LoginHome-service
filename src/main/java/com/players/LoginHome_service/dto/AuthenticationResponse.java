package com.players.LoginHome_service.dto;

import com.players.LoginHome_service.model.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private Long id;
    private UserRole userRole;
}

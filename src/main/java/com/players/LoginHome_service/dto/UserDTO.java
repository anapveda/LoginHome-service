package com.players.LoginHome_service.dto;

import com.players.LoginHome_service.model.enums.UserRole;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private UserRole userRole;
}

package com.players.LoginHome_service.model.Entity;

import com.players.LoginHome_service.dto.UserDTO;
import com.players.LoginHome_service.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userid;
    private String email;
    private String name;
    private String password;
    private UserRole userRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Grant authority based on userRole
        //userRole.name() returns ADMIN/CUSTOMER;
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    //true → the account is enabled (active), so the user can log in.

     // false → the account is disabled, so the user cannot log in even if the credentials are correct.

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public UserDTO getUserDTO() {
        UserDTO u=new UserDTO();
        u.setId(userid);
        u.setEmail(email);
        u.setName(name);
        u.setUserRole(userRole);
        return u;
    }
}

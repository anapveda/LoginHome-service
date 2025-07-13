package com.players.LoginHome_service.Service.AuthenticationService;

import com.players.LoginHome_service.model.Entity.User;
import com.players.LoginHome_service.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService{

    @Autowired
    UserRepo userRepo;


    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepo.findFirstByEmail(username).orElseThrow(()-> new UsernameNotFoundException("user not found"));
            }
        };
    }

    public User findByEmail(String email) {
        return new User();
    }
}

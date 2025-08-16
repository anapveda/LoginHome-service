package com.players.LoginHome_service.Service.AuthenticationService;

import com.players.LoginHome_service.Util.jwtUtil;
import com.players.LoginHome_service.dto.AuthenticationResponse;
import com.players.LoginHome_service.model.Entity.User;
import com.players.LoginHome_service.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{

    @Autowired
    UserRepo userRepo;
    @Autowired
    jwtUtil jwt;

    @Autowired
    Authenticationservice authenticationservice;

    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userRepo.findFirstByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                return new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        user.getAuthorities()
                );
            }
        };
    }


    public ResponseEntity<AuthenticationResponse> validateAndGenerateToken(String email) {
        UserDetails userDetails=userDetailsService().loadUserByUsername(email);
        String role= userDetails.getAuthorities().toString().replace("[","").replace("]","");
        Map<String, Object> claims = new HashMap<>();
        claims.put("userRole", role.replace("ROLE_",""));
        System.out.println(claims);
        String token=jwt.generateToken(claims,userDetails);
        System.out.println(token);
        AuthenticationResponse response=authenticationservice.authenticateLoggedInUser(userDetails.getUsername(),token);
        return ResponseEntity.ok(response);



    }
}
//    public User findByEmail(String email) {
//        Optional<User> u=userRepo.findFirstByEmail(email);
//        User user=new User();
//        if(u.isPresent()){
//            user.setUserid(u.get().getUserid());
//            user.setUserRole(u.get().getUserRole());
//        }
//
//        return user;
//    }
//       //returns userDetials object no DB call is made here
//       UserDetails userDetails=userService.userDetailsService().loadUserByUsername(request.getEmail());
//        User user = userService.findByEmail(request.getEmail());
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userId",user.getUserid());
//        claims.put("userRole", user.getUserRole().name());
//        String token=jwt.generateToken(claims,userDetails);
//       AuthenticationResponse response=authenticationservice.authenticateLoggedInUser(userDetails.getUsername(),token);
//       return ResponseEntity.ok(response);
package com.players.LoginHome_service.Controller;

import com.players.LoginHome_service.Service.AuthenticationService.Authenticationservice;
import com.players.LoginHome_service.Service.AuthenticationService.UserService;
import com.players.LoginHome_service.Util.jwtUtil;
import com.players.LoginHome_service.dto.AuthenticationRequest;
import com.players.LoginHome_service.dto.AuthenticationResponse;
import com.players.LoginHome_service.dto.SignUpRequest;
import com.players.LoginHome_service.dto.UserDTO;
import com.players.LoginHome_service.model.Entity.User;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    Authenticationservice authenticationservice;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;
    @Autowired
    jwtUtil jwt;


    @PostMapping("/signUp")
    public ResponseEntity<?> signUpUser(@RequestBody SignUpRequest signUpRequest){

        try{
            UserDTO user=authenticationservice.createUser(signUpRequest);
            return new ResponseEntity<>(user,HttpStatus.OK);
        }catch(EntityExistsException e){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
/*flowchart TD
    A[User submits login form] --> B[AuthenticationManager]
    B --> C[AuthenticationProvider]
    C --> D[UserDetailsService loads user from DB]
    D --> E[Provider verifies password]
    E --> F[Authentication successful → return token]

    Step	Component	Responsibility
1	AuthenticationManager	Accepts the credentials (email, password)
2	DaoAuthenticationProvider	Delegates to UserDetailsService
3	UserDetailsService	Loads user info from DB
4	DaoAuthenticationProvider	Verifies password with PasswordEncoder
5	AuthenticationManager	Returns Authentication object if successful*/

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        // Authenticate
       try {
           Authentication authentication = authenticationManager.authenticate(

                   new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
           );
       }catch(BadCredentialsException e){
           throw new BadCredentialsException("Incorrect email or password");
       }
       User user = userService.findByEmail(request.getEmail());
       UserDetails userDetails=userService.userDetailsService().loadUserByUsername(request.getEmail());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userRole", request.getUserRole());
        String token=jwt.generateToken(claims,userDetails);
       AuthenticationResponse response=authenticationservice.authenticateLoggedInUser(userDetails.getUsername(),token);
       return ResponseEntity.ok(response);

    }

}

package com.players.LoginHome_service.Controller;

import com.players.LoginHome_service.Service.AuthenticationService.Authenticationservice;
import com.players.LoginHome_service.Service.AuthenticationService.UserService;
import com.players.LoginHome_service.Util.jwtUtil;
import com.players.LoginHome_service.dto.*;
import com.players.LoginHome_service.model.Entity.User;
import com.players.LoginHome_service.repository.UserRepo;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    Authenticationservice authenticationservice;
    @Autowired
    UserRepo userRepository;


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

       return userService.validateAndGenerateToken(request.getEmail());

    }
    @GetMapping("/user/location")
    public ResponseEntity<LocationDTO> getUserLocation(Authentication authentication) {

        String emailFromToken = authentication.getName(); // comes from JWT "sub" claim
        User loggedInUser = userRepository.findFirstByEmail(emailFromToken)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user = userRepository.findById(loggedInUser.getUserid()).orElseThrow(() -> new RuntimeException("User not found"));
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setLatitude(user.getLatitude());
        locationDTO.setLongitude(user.getLongitude());
        return ResponseEntity.ok(locationDTO);
    }
    @PutMapping("/user/location")
    public ResponseEntity<String> updateLocation(@RequestBody LocationDTO dto,Principal principal) {
        User loggedInUser = userRepository.findFirstByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        loggedInUser.setLatitude(dto.getLatitude());
        loggedInUser.setLongitude(dto.getLongitude());
        userRepository.save(loggedInUser);

        return ResponseEntity.ok("Location updated successfully");
    }




}

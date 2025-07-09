package com.players.LoginHome_service.Controller;

import com.players.LoginHome_service.AuthenticationService.Authenticationservice;
import com.players.LoginHome_service.dto.SignUpRequest;
import com.players.LoginHome_service.dto.UserDTO;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    Authenticationservice authenticationservice;
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

}

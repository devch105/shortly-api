package com.devch105.shortly.controller;

import com.devch105.shortly.dto.AuthDTO;
import com.devch105.shortly.dto.RegisterDTO;
import com.devch105.shortly.dto.UserDTO;
import com.devch105.shortly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register( @RequestBody RegisterDTO userdto){
        System.out.println("User Register Request + "+userdto);
        UserDTO user = userService.registerUser(userdto);
        System.out.println("User Register Response "+user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody AuthDTO authDTO){
      try{
          System.out.println("User Login Request + "+authDTO);
          if(authDTO == null ){
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email and Password are required"));
          }
            Map<String,Object> tokenAndUser =  userService.authenticateAndGenerateUser(authDTO);
          System.out.println("User Login Response "+tokenAndUser);
              return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                      "message", "User Login Successfully",
                      "token", tokenAndUser.get("token"),
                      "user", tokenAndUser.get("user")
                      ));
          }catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
      }
    }

}

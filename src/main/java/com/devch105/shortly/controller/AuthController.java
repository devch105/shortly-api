package com.devch105.shortly.controller;

import com.devch105.shortly.dto.RegisterDTO;
import com.devch105.shortly.dto.UserDTO;
import com.devch105.shortly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register( @RequestBody RegisterDTO userdto){
        UserDTO user = userService.registerUser(userdto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}

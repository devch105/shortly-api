package com.devch105.shortly.service;

import com.devch105.shortly.dto.AuthDTO;
import com.devch105.shortly.dto.RegisterDTO;
import com.devch105.shortly.dto.UserDTO;
import com.devch105.shortly.entity.UserEntity;
import com.devch105.shortly.repository.UserRepository;
import com.devch105.shortly.security.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils  jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Transactional
    public UserDTO registerUser(RegisterDTO registerDTO) {

        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists.");
        }
        UserEntity savedUser =
                userRepository.save(toRegisterEntity(registerDTO));
        log.info("User {} registered successfully.", savedUser.getEmail());
        return toDTO(savedUser);
    }

    @Transactional
    public Map<String,Object> authenticateAndGenerateUser (AuthDTO authDTO ) {
       try{
           Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),authDTO.getPassword()));
           SecurityContextHolder.getContext().setAuthentication(authentication);
           UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
           String token = jwtUtils.generateToken(userDetails);

           return Map.of("token", token,"user",getPublicProfile(authDTO.getEmail()));
       } catch (Exception e) {
           throw new UsernameNotFoundException("Invalid username or password.");
       }
    }

    public UserDTO getPublicProfile(String email){
        UserEntity user = null;
        if(email == null && email.isEmpty()){
            user = getCurrentprofile();
        }else {
            user = userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        }
        return toDTO(user);
    }

    private UserEntity getCurrentprofile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userRepository.findByEmail(authentication.getName()).orElseThrow(()-> new UsernameNotFoundException("User not found in database"+authentication.getName()));
        log.info("User {} found successfully.", user.getEmail());
        return user;
    }


    public UserDTO toDTO(UserEntity  userEntity){
        return UserDTO.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .role(userEntity.getRole())
                .build();
    }

    public UserEntity toEntity(UserDTO
                                       userDTO){
        return UserEntity.builder()
//                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .role(UserEntity.Role.ROLE_USER)
                .build();
    }


    public UserEntity toRegisterEntity(RegisterDTO registerDTO){
        return UserEntity.builder()
                .email(registerDTO.getEmail())
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(UserEntity.Role.ROLE_USER)
                .build();
    }


}

package com.devch105.shortly.service;

import com.devch105.shortly.dto.RegisterDTO;
import com.devch105.shortly.dto.UserDTO;
import com.devch105.shortly.entity.UserEntity;
import com.devch105.shortly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserDTO registerUser(RegisterDTO registerDTO) {

        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists.");
        }
        UserEntity savedUser =
                userRepository.save(toEntity(registerDTO));
        log.info("User {} registered successfully.", savedUser.getEmail());
        return toDTO(savedUser);
    }


    public UserDTO toDTO(UserEntity  userEntity){
        return UserDTO.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .username(userEntity.getUsername())
                .role(userEntity.getRole())
                .build();
    }

    public UserEntity toEntity(RegisterDTO
                                       registerDTO){
        return UserEntity.builder()
                .email(registerDTO.getEmail())
                .username(registerDTO.getUsername())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .role(UserEntity.Role.ROLE_USER)
                .build();
    }


}

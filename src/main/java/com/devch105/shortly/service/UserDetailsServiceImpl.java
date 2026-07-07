package com.devch105.shortly.service;


import com.devch105.shortly.entity.UserEntity;
import com.devch105.shortly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

     private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity existingProfile = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email));

        return UserDetailsImpl.build(existingProfile);
    }
}

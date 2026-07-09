package com.devch105.shortly.controller;

import com.devch105.shortly.dto.ClickEventDTO;
import com.devch105.shortly.dto.UrlDTO;
import com.devch105.shortly.dto.UserDTO;
import com.devch105.shortly.entity.UserEntity;
import com.devch105.shortly.repository.UrlRepository;
import com.devch105.shortly.repository.UserRepository;
import com.devch105.shortly.service.UrlService;
import com.devch105.shortly.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/urls")
@RequiredArgsConstructor
@Slf4j
public class UrlController {

    public final UrlService urlService;
    public final UserService userService;
    private final UserRepository userRepository;


    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createShortUrl(
            @RequestBody Map<String, String> request,
            Principal principal) {

        try {
            String originalUrl = request.get("originalUrl");
            log.info("Principal Name = " + principal.getName());
            UserDTO user = userService.getPublicProfile(principal.getName());
            UserEntity userEntity =  userRepository.findByEmail(user.getEmail()).orElseThrow(()-> new UsernameNotFoundException("User not found"));

            UrlDTO urlDTO = urlService.createShortUrl(originalUrl, userEntity);
            log.info("originalUrl = " + originalUrl);
            log.info("shortUrl = " + urlDTO.getShortUrl());
            return ResponseEntity.ok(urlDTO);

        } catch (Exception e) {
            e.printStackTrace();   // <---- IMPORTANT
            throw e;
        }
    }


    @GetMapping
    public  ResponseEntity<List<UrlDTO>> getUserUrls(Principal principal) {
       try{
           UserDTO user = userService.getPublicProfile(principal.getName());
           List<UrlDTO> currentUserUrls = urlService.geUrlsOfCurrentUser(user);
           return ResponseEntity.ok(currentUserUrls);
       }catch (Exception e){
           throw new RuntimeException("Error getting urls for current user : " + principal.getName(), e);
       }
    }


    @GetMapping("/analytics/{shortcode}")
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(Principal principal,
                                                               @PathVariable String shortcode,
                                                               @RequestParam("startDate") String startDate,
                                                               @RequestParam("endDate") String endDate
                                                               ) {

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime startDateTime = LocalDateTime.parse(startDate, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate, formatter);

       List<ClickEventDTO> clickEventDTOS  = urlService.getClickEventsByDate(shortcode,startDateTime,endDateTime);
       return  ResponseEntity.ok(clickEventDTOS);
    }
}

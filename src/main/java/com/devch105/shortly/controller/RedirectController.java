package com.devch105.shortly.controller;

import com.devch105.shortly.dto.UrlDTO;
import com.devch105.shortly.entity.UrlEntity;
import com.devch105.shortly.service.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RedirectController {

    private final UrlService urlService;

    @GetMapping("/{url}")
    public ResponseEntity<Void> redirectTo(@PathVariable String url) {
        UrlDTO originalUrl = urlService.getOriginalUrl(url);
        if (originalUrl == null) {
            return ResponseEntity.notFound().build();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION,originalUrl.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}

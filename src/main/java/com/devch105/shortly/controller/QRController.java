package com.devch105.shortly.controller;

import com.devch105.shortly.service.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QRController {

    private  final QRCodeService qrCodeService;

    @Value("${app.backend.base.url}")
    private String backendUrl;



    @GetMapping(value = "/{shortUrl}",
    produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> getQR(@PathVariable String shortUrl){


        String finalUrl = backendUrl +"/"+ shortUrl;

        byte[] image = qrCodeService.generateQRCode(finalUrl,500,500);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }
}

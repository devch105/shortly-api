package com.devch105.shortly.dto;

import com.devch105.shortly.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UrlDTO {
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private Long clickCount;
    private LocalDateTime createdDate;
    private String email;
}

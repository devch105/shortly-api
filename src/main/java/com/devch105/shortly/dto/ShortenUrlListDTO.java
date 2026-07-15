package com.devch105.shortly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortenUrlListDTO {

    public Long id;
    public String shortUrl;
    public String longUrl;
    public String shortCode;
    private Long clickCount;
    private LocalDateTime createdDate;

}
